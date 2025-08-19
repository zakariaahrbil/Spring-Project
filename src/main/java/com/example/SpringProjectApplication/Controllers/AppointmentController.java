package com.example.SpringProjectApplication.Controllers;

import com.example.SpringProjectApplication.Dtos.AppointmentDto;
import com.example.SpringProjectApplication.Response.ResponseTemplate;
import com.example.SpringProjectApplication.Services.AppointmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@AllArgsConstructor
public class AppointmentController

{

    private final AppointmentService appointmentService;

    @GetMapping("/{appointmentId}")
    public ResponseEntity<ResponseTemplate<AppointmentDto>> getAppointmentById(@PathVariable Long appointmentId)
    {
        try {
            if (appointmentId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment ID cannot be null");
            }
            return new ResponseEntity<>(appointmentService.getAppointmentById(appointmentId), HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while fetching the appointment", null, false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<List<AppointmentDto>>> getAllAppointments()
    {
        try {
            return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while fetching all appointments", null, false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #patientId == principal.id)")
    public ResponseEntity<ResponseTemplate<List<AppointmentDto>>> getAppointmentsByPatientId(@PathVariable Long patientId)
    {
        try {
            if (patientId == null) {
                return new ResponseEntity<>(new ResponseTemplate<>("Patient ID cannot be null", null, false), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(appointmentService.getAppointmentsByPatientId(patientId), HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while fetching appointments for the patient", null, false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseTemplate<AppointmentDto>> createAppointment(@Valid @RequestBody AppointmentDto appointmentDto)
    {
        try {
            return new ResponseEntity<>(appointmentService.createAppointment(appointmentDto), HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while creating the appointment", null, false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping("/cancel/{appointmentId}")
    public ResponseEntity<ResponseTemplate<Void>> cancelAppointment(@PathVariable Long appointmentId)
    {
        try {
            return new ResponseEntity<>(appointmentService.cancelAppointment(appointmentId), HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while canceling the appointment", null, false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/confirm/{appointmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<Void>> confirmAppointment(@PathVariable Long appointmentId)
    {
        try{
            if (appointmentId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment ID cannot be null");
            }
            return new ResponseEntity<>(appointmentService.confirmAppointment(appointmentId), HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        }catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while confirming the appointment", null, false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{appointmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<AppointmentDto>> rescheduleAppointment(
            @PathVariable String appointmentId, @RequestParam("newDateTime") String newDateTime)
    {
        try {
            if (appointmentId == null || newDateTime == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment ID and new date/time cannot be null");
            }
            LocalDateTime newDateTimeParsed = LocalDateTime.parse(newDateTime);
            Long appointmentIdParsed = Long.parseLong(appointmentId);
            return new ResponseEntity<>(appointmentService.rescheduleAppointment(appointmentIdParsed, newDateTimeParsed), HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while rescheduling the appointment", null, false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
