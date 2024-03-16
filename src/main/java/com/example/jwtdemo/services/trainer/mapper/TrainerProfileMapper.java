package com.example.jwtdemo.services.trainer.mapper;

import com.example.jwtdemo.entities.Trainer;
import com.example.jwtdemo.models.profiles.TrainerProfile;
import com.example.jwtdemo.services.ProfileMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrainerProfileMapper extends ProfileMapper<Trainer, TrainerProfile> {

    @Mapping(source = "user.firstName",target = "firstname")
    @Mapping(source = "user.lastName",target = "lastname")
    @Mapping(source = "user.username",target = "username")
    TrainerProfile apply(Trainer trainer);
}
