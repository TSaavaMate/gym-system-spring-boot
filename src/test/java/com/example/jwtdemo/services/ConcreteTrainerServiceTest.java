package com.example.jwtdemo.services;

import com.example.jwtdemo.entities.Trainer;
import com.example.jwtdemo.entities.User;
import com.example.jwtdemo.exceptions.ResourceNotFoundException;
import com.example.jwtdemo.models.requests.patchRequest.PatchTrainer;
import com.example.jwtdemo.repositories.TrainerRepository;
import com.example.jwtdemo.services.trainer.ConcreteTrainerService;
import com.example.jwtdemo.services.trainer.mapper.TrainerDtoMapper;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcreteTrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private ConcreteTrainerService.TrainerRequestValidator requestMapper;

    @Mock
    private TrainerDtoMapper dtoMapper;

    @InjectMocks
    private ConcreteTrainerService trainerService;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Test
    void testFindById() {
        Long id = 1L;
        Trainer trainer = new Trainer();
        when(trainerRepository.findById(id)).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
    }

    @Test
    void testDelete() {
        Long id = 1L;
        doNothing().when(trainerRepository).deleteById(id);

        assertDoesNotThrow(() -> trainerService.delete(id));
    }

    @Test
    void testSetActiveState() {
        String username = "testUser";
        Boolean isActive = true;
        Trainer trainer = new Trainer();
        trainer.setUser(User.builder().build());
        PatchTrainer request = new PatchTrainer();
        request.setUsername(username);
        request.setIsActive(true);
        when(trainerRepository.findTrainerByUserUsername(username)).thenReturn(Optional.of(trainer));

        assertDoesNotThrow(() -> trainerService.setActiveState(request));

        assertEquals(isActive, trainer.getUser().getIsActive());
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void testSetActiveState_NonExistentTrainer() {
        String username = "nonexistentUser";
        PatchTrainer request = new PatchTrainer();
        request.setUsername(username);
        request.setIsActive(true);
        when(trainerRepository.findTrainerByUserUsername(username)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> trainerService.setActiveState(request));
    }

    @Test
    void testSetActiveState_ComplexBehavior() {
        String username = "testUser";
        Trainer trainer = new Trainer();
        trainer.setUser(User.builder().isActive(false).build());
        PatchTrainer request = new PatchTrainer();
        request.setUsername(username);
        request.setIsActive(true);
        when(trainerRepository.findTrainerByUserUsername(username)).thenReturn(Optional.of(trainer));

        assertDoesNotThrow(() -> trainerService.setActiveState(request));

        assertTrue(trainer.getUser().getIsActive());
        verify(trainerRepository, times(1)).save(trainer);
    }
}



