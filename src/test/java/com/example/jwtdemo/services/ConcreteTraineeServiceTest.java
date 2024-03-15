package com.example.jwtdemo.services;

import com.example.jwtdemo.entities.Trainee;
import com.example.jwtdemo.entities.User;
import com.example.jwtdemo.exceptions.ResourceNotFoundException;
import com.example.jwtdemo.models.dto.TraineeDto;
import com.example.jwtdemo.models.requests.patchRequest.PatchTrainee;
import com.example.jwtdemo.repositories.TraineeRepository;
import com.example.jwtdemo.services.trainee.ConcreteTraineeService;
import com.example.jwtdemo.services.trainee.mapper.TraineeDtoMapper;
import com.example.jwtdemo.services.trainee.mapper.TraineeRequestMapper;
import com.example.jwtdemo.services.trainee.traineeTraining.TraineeTrainingService;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcreteTraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TraineeTrainingService traineeTrainingService;

    @Mock
    private TraineeRequestMapper requestMapper;

    @Mock
    private TraineeDtoMapper dtoMapper;

    @InjectMocks
    private ConcreteTraineeService traineeService;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Test
    void testFindById() {
        Long id = 1L;
        Trainee trainee = new Trainee();
        when(traineeRepository.findById(id)).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
    }

    @Test
    void testFindByUsername() {
        String username = "testUser";
        TraineeDto traineeDto = TraineeDto.builder().firstname("nm").build();

        when(traineeRepository.findTraineeByUserUsername(username))
                .thenReturn(Optional.of(new Trainee()));

        when(traineeTrainingService.getTraineeTrainers(any()))
                .thenReturn(new ArrayList<>());

        when(dtoMapper.apply(any()))
                .thenReturn(traineeDto);

        TraineeDto result = traineeService.findByUsername(username);

        assertNotNull(result);
        assertEquals(traineeDto, result);
    }

    @Test
    void testFindByUsername_ThrowsException() {
        String username = "nonexistentUser";
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> traineeService.findByUsername(username));
    }
    @Test
    void testDelete() {
        Long id = 1L;
        doNothing().when(traineeRepository).deleteById(id);

        assertDoesNotThrow(() -> traineeService.delete(id));
    }

    @Test
    void testSetActiveState() {
        String username = "testUser";
        PatchTrainee request = new PatchTrainee();
        request.setUsername(username);
        request.setIsActive(true);

        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.of(trainee));

        assertDoesNotThrow(() -> traineeService.setActiveState(request));

        assertTrue(trainee.getUser().getIsActive());
        verify(traineeRepository, times(1)).save(trainee);
    }


}
