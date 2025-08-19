package com.example.SpringProjectApplication.Services;

import com.example.SpringProjectApplication.Dtos.AppointmentDto;
import com.example.SpringProjectApplication.Response.ResponseTemplate;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService
{
    ResponseTemplate<AppointmentDto> getAppointmentById(Long appointmentId);
    ResponseTemplate<List<AppointmentDto>> getAppointmentsByPatientId(Long patientId);
    ResponseTemplate<AppointmentDto> getAppointmentByVisitRecordId(Long visitRecordId);

    ResponseTemplate<List<AppointmentDto>> getAllAppointments();

    ResponseTemplate<AppointmentDto> createAppointment(AppointmentDto appointment);

    ResponseTemplate<Void> cancelAppointment(Long appointmentId);

    ResponseTemplate<AppointmentDto> rescheduleAppointment(Long appointmentId, LocalDateTime newDateTime);

    ResponseTemplate<Void> confirmAppointment(Long appointmentId);

}
