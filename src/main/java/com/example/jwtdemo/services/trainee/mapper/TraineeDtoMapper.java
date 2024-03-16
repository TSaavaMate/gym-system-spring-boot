package com.example.jwtdemo.services.trainee.mapper;

import com.example.jwtdemo.entities.Trainee;
import com.example.jwtdemo.models.dto.TraineeDto;
import com.example.jwtdemo.services.DtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TraineeDtoMapper extends DtoMapper<Trainee, TraineeDto> {
    @Mapping(source = "user.firstName",target = "firstname")
    @Mapping(source = "user.lastName",target = "lastname")
    @Mapping(source = "user.isActive",target = "isActive")
    TraineeDto apply(Trainee trainee) ;
}
