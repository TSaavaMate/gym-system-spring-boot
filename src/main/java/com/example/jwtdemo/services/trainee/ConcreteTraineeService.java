package com.example.jwtdemo.services.trainee;

import com.example.jwtdemo.aspect.Loggable;
import com.example.jwtdemo.entities.Trainee;
import com.example.jwtdemo.entities.User;
import com.example.jwtdemo.exceptions.ResourceNotFoundException;
import com.example.jwtdemo.models.dto.TraineeDto;
import com.example.jwtdemo.models.profiles.TrainerProfile;
import com.example.jwtdemo.models.requests.patchRequest.PatchTraineeRequest;
import com.example.jwtdemo.models.requests.registrationRequest.RegistrationRequest;
import com.example.jwtdemo.models.requests.registrationRequest.TraineeRegistrationRequest;
import com.example.jwtdemo.models.requests.updateRequest.UpdateTraineeRequest;
import com.example.jwtdemo.models.requests.updateRequest.UpdateTraineeTrainersRequest;
import com.example.jwtdemo.models.responses.RegistrationResponse;
import com.example.jwtdemo.repositories.TraineeRepository;
import com.example.jwtdemo.services.trainee.mapper.TraineeDtoMapper;
import com.example.jwtdemo.services.trainee.mapper.TraineeRequestMapper;
import com.example.jwtdemo.services.trainee.traineeTraining.TraineeTrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConcreteTraineeService implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final TraineeTrainingService traineeTrainingService;
    private final TraineeRequestMapper requestMapper;
    private final TraineeDtoMapper dtoMapper;


    @Override
    public Optional<Trainee> findById(Long id) {
        return traineeRepository.findById(id);
    }

    @Override
    public TraineeDto findByUsername(String username) {
        var trainee = traineeRepository.findTraineeByUserUsername(username)
                .orElseThrow(ResourceNotFoundException::new);

        var trainers = traineeTrainingService.getTraineeTrainers(trainee);

        var traineeDto = dtoMapper.apply(trainee);

        traineeDto.setTrainers(trainers);

        return traineeDto;
    }

    @Override
    public Collection<Trainee> findAll() {
        return traineeRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        traineeRepository.deleteById(id);
        log.info("Deleted trainee with ID: {}", id);
    }

    @Override
    public void setActiveState(PatchTraineeRequest request) {
        var trainee = traineeRepository.findTraineeByUserUsername(request.getUsername())
                .orElseThrow(ResourceNotFoundException::new);

        User user = trainee.getUser();
        user.setIsActive(request.getIsActive());

        traineeRepository.save(trainee);
    }

    @Override
    @Loggable
    public TraineeDto update(@Validated UpdateTraineeRequest request) {
        var trainee = traineeRepository.findTraineeByUserUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("Not found trainee with username: {}", request.getUsername());
                    return new ResourceNotFoundException("Not found trainee with username: " + request.getUsername());
                });

        var date = request.getDate_of_birth();

        if (date != null) {
            trainee.setDateOfBirth(request.getDate_of_birth());
        }

        var address = request.getAddress();
        if (address != null) {
            trainee.setAddress(request.getAddress());
        }

        var user = trainee.getUser();
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setIsActive(request.getIsActive());

        trainee.setUser(user);

        traineeRepository.save(trainee);

        log.info("Updated trainee with ID: {}", trainee.getId());

        var updated = traineeRepository.findById(trainee.getId()).orElseThrow();

        return dtoMapper.apply(updated);
    }

    @Override
    public List<TrainerProfile> updateTraineeTrainers(UpdateTraineeTrainersRequest request) {
        var username = request.getTraineeUsername();
        var trainee = traineeRepository.findTraineeByUserUsername(username)
                .orElseThrow(() -> {
                    var message = "Not found trainee with username: " +  username ;

                    log.warn(message);

                    return new ResourceNotFoundException(message);
                });

        return traineeTrainingService.updateTraineeTrainers(trainee, request.getTrainers());
    }

    @Override
    public RegistrationResponse create(RegistrationRequest request) {
        var trainee = requestMapper.apply((TraineeRegistrationRequest) request);
        traineeRepository.save(trainee);
        return RegistrationResponse.builder()
                .username(trainee.getUser().getUsername())
                .password(trainee.getUser().getPassword())
                .build();
    }
}
