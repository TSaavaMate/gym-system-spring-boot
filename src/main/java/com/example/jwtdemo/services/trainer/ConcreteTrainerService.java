package com.example.jwtdemo.services.trainer;

import com.example.jwtdemo.aspect.Loggable;
import com.example.jwtdemo.entities.Trainer;
import com.example.jwtdemo.entities.User;
import com.example.jwtdemo.exceptions.ResourceNotFoundException;
import com.example.jwtdemo.models.dto.TrainerDto;
import com.example.jwtdemo.models.profiles.TrainerProfile;
import com.example.jwtdemo.models.requests.patchRequest.PatchTrainer;
import com.example.jwtdemo.models.requests.registrationRequest.Registration;
import com.example.jwtdemo.models.requests.registrationRequest.TrainerRegistration;
import com.example.jwtdemo.models.requests.trainerFilterRequest.ActiveTrainers;
import com.example.jwtdemo.models.requests.updateRequest.UpdateTrainer;
import com.example.jwtdemo.models.responses.RegistrationResponse;
import com.example.jwtdemo.repositories.TrainerRepository;
import com.example.jwtdemo.repositories.UserRepository;
import com.example.jwtdemo.services.trainer.mapper.TrainerDtoMapper;
import com.example.jwtdemo.services.trainer.mapper.TrainerProfileMapper;
import com.example.jwtdemo.services.trainer.trainerTraining.TrainerTrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConcreteTrainerService implements TrainerService{

    private final TrainerRepository trainerRepository;
    private final TrainerRequestValidator requestMapper;
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
        if (trainerRepository.existsById(id)) throw new ResourceNotFoundException();

        trainerRepository.deleteById(id);
        log.info("deleted trainee with ID: {}", id);
    }
    @Override
    @Loggable
    @Transactional
    public TrainerDto update(@Validated UpdateTrainer request) {
        var trainer = trainerRepository.findTrainerByUserUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("not found trainer with username : {}" , request.getUsername());
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

        log.info("updated training with ID: {}", trainer.getId());

        var trainees = trainerTrainingService.getTrainerTrainees(trainer);

        var trainerDto = dtoMapper.apply(trainer);
        trainerDto.setTrainees(trainees);

        return trainerDto;


    }

    @Override
    public List<TrainerProfile> getActiveTrainers(ActiveTrainers request) {
        return trainerRepository.findTrainersByUserUsernameAndUserIsActive(request.getUsername(),true).stream()
                .map(profileMapper)
                .toList();

    }

    @Override
    public void setActiveState(PatchTrainer request) {
        var trainer = trainerRepository.findTrainerByUserUsername(request.getUsername())
                .orElseThrow(ResourceNotFoundException::new);

        User user = trainer.getUser();
        user.setIsActive(request.getIsActive());

        trainerRepository.save(trainer);
    }

    @Override
    public RegistrationResponse create(Registration request) {
        var trainer = requestMapper.apply((TrainerRegistration) request);

        trainerRepository.save(trainer);

        return RegistrationResponse.builder()
                .username(trainer.getUser().getUsername())
                .password(trainer.getUser().getPassword())
                .build();
    }


    @Component
    @RequiredArgsConstructor
    public static class TrainerRequestValidator implements Function<TrainerRegistration, Trainer> {
        private final UserRepository userRepository;
        @Override
        public Trainer apply(TrainerRegistration request) {
            var user = userRepository.findByFirstNameAndLastName(request.getFirstname(), request.getLastname())
                    .orElseThrow(ResourceNotFoundException::new);
            return new Trainer(user,request.getSpecialization());
        }
    }
}
