package com.example.jwtdemo.services.trainee.mapper;

import com.example.jwtdemo.entities.Trainee;
import com.example.jwtdemo.models.profiles.TraineeProfile;
import com.example.jwtdemo.services.ProfileMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TraineeProfileMapper extends ProfileMapper<Trainee, TraineeProfile> {

    @Mapping(source = "user.firstName",target = "firstname")
    @Mapping(source = "user.lastName",target = "lastname")
    @Mapping(source = "user.username",target = "username")
    TraineeProfile apply(Trainee trainee);
}
