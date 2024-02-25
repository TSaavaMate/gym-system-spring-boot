package com.example.jwtdemo.services.trainer;

import com.example.jwtdemo.entities.Trainer;
import com.example.jwtdemo.entities.User;
import com.example.jwtdemo.exceptions.ResourceNotFoundException;
import com.example.jwtdemo.models.dto.TrainerDto;
import com.example.jwtdemo.models.profiles.TrainerProfile;
import com.example.jwtdemo.models.requests.registrationRequest.RegistrationRequest;
import com.example.jwtdemo.models.requests.registrationRequest.TrainerRegistrationRequest;
import com.example.jwtdemo.models.requests.trainerFilterRequest.ActiveTrainersRequest;
import com.example.jwtdemo.models.requests.updateRequest.UpdateTrainerRequest;
import com.example.jwtdemo.models.responses.RegistrationResponse;
import com.example.jwtdemo.repositories.TrainerRepository;
import com.example.jwtdemo.services.trainer.mapper.TrainerDtoMapper;
import com.example.jwtdemo.services.trainer.mapper.TrainerProfileMapper;
import com.example.jwtdemo.services.trainer.mapper.TrainerRequestMapper;
import com.example.jwtdemo.services.trainer.trainerTraining.TrainerTrainingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConcreteTrainerService implements TrainerService{
    private final Logger logger = LoggerFactory.getLogger(ConcreteTrainerService.class);

    private final TrainerRepository trainerRepository;
    private final TrainerRequestMapper requestMapper;
    private final TrainerDtoMapper dtoMapper;
    private final TrainerProfileMapper profileMapper;

    private final TrainerTrainingService trainerTrainingService;

    @Override
    public Optional<Trainer> findById(Long id) {
        return trainerRepository.findById(id);
    }

    @Override
    public TrainerDto findByUsername(String username) {
        var trainer = trainerRepository.findTrainerByUserUsername(username)
                .orElseThrow(ResourceNotFoundException::new);

        var trainees = trainerTrainingService.getTrainerTrainees(trainer);

        var trainerDto = dtoMapper.apply(trainer);
        trainerDto.setTrainees(trainees);

        return trainerDto;
    }

    @Override
    public Collection<Trainer> findAll() {
        return trainerRepository.findAll();
    }

    @Override
    public void delete(Long id) {

        trainerRepository.deleteById(id);
        logger.info("deleted trainee with ID: {}", id);
    }

    @Override
    public Trainer SetActiveState(Long id, Boolean state) {
        Trainer trainer = trainerRepository.findById(id).orElseThrow();

        trainer.getUser().setIsActive(state);

        trainerRepository.save(trainer);

        return trainerRepository.findById(id).orElseThrow();
    }

    @Override
    public TrainerDto update(@Validated UpdateTrainerRequest request) {
        var trainer = trainerRepository.findTrainerByUserUsername(request.getUsername())
                .orElseThrow(() -> {
                    logger.warn("not found trainer with username : {}" , request.getUsername());
                    return new ResourceNotFoundException("not found trainer with username :" + request.getUsername());
                });


        User user = trainer.getUser();
        user.setUsername(request.getUsername());
        user.setLastName(request.getLastname());
        user.setFirstName(request.getFirstname());
        user.setIsActive(request.getIsActive());

        trainer.setUser(user);
        trainer.setSpecialization(request.getSpecialization());

        trainerRepository.save(trainer);

        logger.info("updated training with ID: {}", trainer.getId());

        var trainees = trainerTrainingService.getTrainerTrainees(trainer);

        var trainerDto = dtoMapper.apply(trainer);
        trainerDto.setTrainees(trainees);

        return trainerDto;


    }

    @Override
    public List<TrainerProfile> getActiveTrainers(ActiveTrainersRequest request) {
        return trainerRepository.findTrainersByUserUsernameAndUserIsActive(request.getUsername(),true).stream()
                .map(profileMapper)
                .toList();

    }

    @Override
    public RegistrationResponse create(RegistrationRequest request) {
        var trainer = requestMapper.apply((TrainerRegistrationRequest) request);

        trainerRepository.save(trainer);

        return RegistrationResponse.builder()
                .username(trainer.getUser().getUsername())
                .password(trainer.getUser().getPassword())
                .build();
    }


}
