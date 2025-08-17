package com.example.SpringProjectApplication.Mappers;

import com.example.SpringProjectApplication.Dtos.AppointmentDto;
import com.example.SpringProjectApplication.Entities.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper
{
        Appointment toEntity(AppointmentDto appointmentDto);
        AppointmentDto toDto(Appointment appointment);
}
