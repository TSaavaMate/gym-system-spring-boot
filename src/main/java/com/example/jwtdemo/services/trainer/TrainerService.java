package com.example.jwtdemo.services.trainer;

import com.example.jwtdemo.entities.Trainer;
import com.example.jwtdemo.models.dto.TrainerDto;
import com.example.jwtdemo.models.profiles.TrainerProfile;
import com.example.jwtdemo.models.requests.patchRequest.PatchTrainer;
import com.example.jwtdemo.models.requests.trainerFilterRequest.ActiveTrainers;
import com.example.jwtdemo.models.requests.updateRequest.UpdateTrainer;
import com.example.jwtdemo.services.DaoService;

import java.util.List;

public interface TrainerService extends DaoService<Trainer, TrainerDto, Long> {
    TrainerDto update(UpdateTrainer request);

    List<TrainerProfile> getActiveTrainers(ActiveTrainers request);

    void setActiveState(PatchTrainer request);
}
