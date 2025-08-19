package com.example.SpringProjectApplication.Mappers;

import com.example.SpringProjectApplication.Dtos.VisitRecordDto;
import com.example.SpringProjectApplication.Entities.VisitRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VisitRecordMapper
{
    VisitRecord toEntity(VisitRecordDto visitRecordDto);
    default VisitRecordDto toDto(VisitRecord visitRecord) {
        if (visitRecord == null) {
            return null;
        }

        return VisitRecordDto.builder()
                .type(visitRecord.getType())
                .comment(visitRecord.getComment())
                .prescription(visitRecord.getPrescription())
                .appointmentId(visitRecord.getAppointment() != null ? visitRecord.getAppointment().getId() : null)
                .patientId(visitRecord.getPatient() != null ? visitRecord.getPatient().getId() : null)
                .build();
    }

}
