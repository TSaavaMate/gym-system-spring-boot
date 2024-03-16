package com.example.jwtdemo.services.trainer.mapper;

import com.example.jwtdemo.entities.Trainer;
import com.example.jwtdemo.models.dto.TrainerDto;
import com.example.jwtdemo.services.DtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrainerDtoMapper extends DtoMapper<Trainer, TrainerDto> {

    @Mapping(source = "user.firstName",target = "firstname")
    @Mapping(source = "user.lastName",target = "lastname")
    @Mapping(source = "user.isActive",target = "isActive")
    TrainerDto apply(Trainer trainer);
}
