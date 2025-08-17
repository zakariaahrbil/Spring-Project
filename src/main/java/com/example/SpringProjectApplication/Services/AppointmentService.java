package com.example.SpringProjectApplication.Services;

import com.example.SpringProjectApplication.Dtos.AppointmentDto;
import com.example.SpringProjectApplication.Entities.Appointment;
import com.example.SpringProjectApplication.Entities.AppointmentType;
import com.example.SpringProjectApplication.Response.ResponseTemplate;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService
{
    ResponseTemplate<Appointment> getAppointmentById(Long appointmentId);
    ResponseTemplate<List<Appointment>> getAppointmentByPatientId(Long patientId);
    ResponseTemplate<Appointment> getAppointmentByVisitRecordId(Long visitRecordId);

    ResponseTemplate<List<Appointment>> getAllAppointments();

    ResponseTemplate<Appointment> createAppointment(AppointmentDto appointment);

    ResponseTemplate<Void> cancelAppointment(Long appointmentId);

    ResponseTemplate<Appointment> rescheduleAppointment(Long appointmentId, LocalDateTime newDateTime);

    ResponseTemplate<Void> confirmAppointment(Long appointmentId);

}
