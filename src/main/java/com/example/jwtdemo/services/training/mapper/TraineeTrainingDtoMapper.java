package com.example.jwtdemo.services.training.mapper;

import com.example.jwtdemo.entities.Training;
import com.example.jwtdemo.models.dto.TraineeTrainingDto;
import com.example.jwtdemo.services.DtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TraineeTrainingDtoMapper extends DtoMapper<Training, TraineeTrainingDto> {
    @Mapping(source = "date", target = "trainingDate")
    @Mapping(source = "trainingType.trainingTypeName", target = "trainingType")
    @Mapping(source = "trainer.user.firstName", target = "trainerName")
    TraineeTrainingDto apply(Training training);

}
