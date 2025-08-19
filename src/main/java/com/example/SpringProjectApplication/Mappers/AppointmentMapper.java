package com.example.SpringProjectApplication.Mappers;

import com.example.SpringProjectApplication.Dtos.AppointmentDto;
import com.example.SpringProjectApplication.Entities.Appointment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper
{
        Appointment toEntity(AppointmentDto appointmentDto);
        default AppointmentDto toDto(Appointment appointment){
            if (appointment == null) {
                return null;
            }

        return AppointmentDto.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatient().getId())
                .type(appointment.getType())
                .confirmedDateTime(appointment.getConfirmedDateTime())
                .status(appointment.getStatus())
                .createdAt(appointment.getCreatedAt())
                .meetingLink(appointment.getMeetingLink())
                .startTime(appointment.getStartTime())
                .proposedDateTime(appointment.getProposedDateTime())
                .reason(appointment.getReason())
                .visitRecordId(appointment.getVisitRecord() != null ? appointment.getVisitRecord().getId() : null)
                .build();
        }

    List<AppointmentDto> toDtoList(List<Appointment> appointments);
}
