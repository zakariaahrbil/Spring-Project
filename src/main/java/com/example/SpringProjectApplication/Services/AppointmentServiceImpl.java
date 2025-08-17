package com.example.SpringProjectApplication.Services;

import com.example.SpringProjectApplication.Dtos.AppointmentDto;
import com.example.SpringProjectApplication.Entities.Appointment;
import com.example.SpringProjectApplication.Entities.Status;
import com.example.SpringProjectApplication.Entities.User;
import com.example.SpringProjectApplication.Entities.VisitRecord;
import com.example.SpringProjectApplication.Mappers.AppointmentMapper;
import com.example.SpringProjectApplication.Repositories.AppointmentRepository;
import com.example.SpringProjectApplication.Repositories.UserRepository;
import com.example.SpringProjectApplication.Repositories.VisitRecordRepository;
import com.example.SpringProjectApplication.Response.ResponseTemplate;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl
        implements AppointmentService
{

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    private final AppointmentMapper appointmentMapper;
    private final VisitRecordRepository visitRecordRepository;

    @Override
    public ResponseTemplate<Appointment> getAppointmentById(Long appointmentId)
    {
        try{
            Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
            if (appointment.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Appointment not found"
                );
            }
            Appointment savedAppointment = appointment.get();

            return new ResponseTemplate<>(
                    "Appointment retrieved successfully",
                    savedAppointment,
                    true
            );

        }catch(ResponseStatusException e){
            throw e;
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while getting the appointment",
                    e
            );
        }
    }

    @Override
    public ResponseTemplate<List<Appointment>> getAppointmentByPatientId(Long patientId)
    {
        try{
            Optional<User> user = userRepository.findById(patientId);
            if (user.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Patient not found"
                );
            }
            List<Appointment> appointments = appointmentRepository.findByPatient(user.get());

            if (appointments.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No appointments found for this patient"
                );
            }
            return new ResponseTemplate<>(
                    "Appointments retrieved successfully",
                    appointments,
                    true
            );


        }catch(ResponseStatusException e){
            throw e;
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while creating the appointment",
                    e
            );
        }
    }

    @Override
    public ResponseTemplate<Appointment> getAppointmentByVisitRecordId(Long visitRecordId)
    {
        try{
            Optional<VisitRecord> visitRecord = visitRecordRepository.findById(visitRecordId);
            if (visitRecord.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Visit record not found"
                );
            }
            Optional<Appointment> appointment = appointmentRepository.findByVisitRecord(visitRecord.get());
            if (appointment.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No appointment found for this visit record"
                );
            }
            return new ResponseTemplate<>(
                    "Appointment retrieved successfully",
                    appointment.get(),
                    true
            );

        }catch(ResponseStatusException e){
            throw e;
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while getting the appointment",
                    e
            );
        }
    }

    @Override
    public ResponseTemplate<List<Appointment>> getAllAppointments()
    {
        try{
            List<Appointment> appointments = appointmentRepository.findAll();
            if (appointments.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No appointments found"
                );
            }
            return new ResponseTemplate<>(
                    "All appointments retrieved successfully",
                    appointments,
                    true
            );

        }catch(ResponseStatusException e){
            throw e;
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while getting all appointments",
                    e
            );
        }
    }

    @Override
    public ResponseTemplate<Appointment> createAppointment(AppointmentDto appointment)
    {
        try{
            if (appointment == null){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Appointment details cannot be null"
                );
            }
            Appointment newAppointment = appointmentMapper.toEntity(appointment);
            Appointment savedAppointment = appointmentRepository.save(newAppointment);

            return new ResponseTemplate<>("Appointment created successfully", savedAppointment, true);

        }catch(ResponseStatusException e){
            throw e;
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while creating the appointment",
                    e
            );
        }
    }

    @Override
    public ResponseTemplate<Void> cancelAppointment(Long appointmentId)
    {
        try{
            Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
            if (appointment.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Appointment not found"
                );
            }
            Appointment savedAppointment = appointment.get();
            savedAppointment.setStatus(Status.CANCELED);
            appointmentRepository.save(savedAppointment);

            return new ResponseTemplate<>("Appointment canceled successfully", null, true);

        }catch(ResponseStatusException e){
            throw e;
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while canceling the appointment",
                    e
            );
        }
    }

    @Override
    public ResponseTemplate<Appointment> rescheduleAppointment(Long appointmentId, LocalDateTime newDateTime)
    {
        try{
            Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
            if (appointment.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Appointment not found"
                );
            }
            if (newDateTime.isBefore(LocalDateTime.now())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "New date/time cannot be in the past"
                );
            }
            Appointment savedAppointment = appointment.get();
            savedAppointment.setProposedDateTime(newDateTime);
            appointmentRepository.save(savedAppointment);

            return new ResponseTemplate<>("Appointment rescheduled successfully", savedAppointment, true);

        }
        catch (ResponseStatusException e){
            throw e;
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while rescheduling the appointment",
                    e
            );
        }
    }

    @Override
    public ResponseTemplate<Void> confirmAppointment(Long appointmentId)
    {
        try{
            Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
            if (appointment.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Appointment not found"
                );
            }
            Appointment savedAppointment = appointment.get();
            savedAppointment.setStatus(Status.CONFIRMED);
            savedAppointment.setConfirmedDateTime(LocalDateTime.now());
            appointmentRepository.save(savedAppointment);

            return new ResponseTemplate<>("Appointment confirmed successfully", null, true);

        }catch (ResponseStatusException e){
            throw e;
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while confirming the appointment",
                    e
            );
        }

    }



}
