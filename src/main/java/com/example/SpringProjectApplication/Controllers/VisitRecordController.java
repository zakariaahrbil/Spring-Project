package com.example.SpringProjectApplication.Controllers;

import com.example.SpringProjectApplication.Dtos.VisitRecordDto;
import com.example.SpringProjectApplication.Response.ResponseTemplate;
import com.example.SpringProjectApplication.Services.VisitRecordService;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/visits")
public class VisitRecordController
{
    private final VisitRecordService visitRecordService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<VisitRecordDto>> createVisitRecord(@Valid @RequestBody VisitRecordDto visitRecordDto)
    {
        try {
            return new ResponseEntity<>(visitRecordService.createVisitRecord(visitRecordDto), HttpStatus.CREATED);
        }
        catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while creating the visit record", null, false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseTemplate<VisitRecordDto>> getVisitRecordById(@PathVariable Long id)
    {
        try {
            if (id == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Visit Record ID cannot be null");
            }
            return new ResponseEntity<>(visitRecordService.getVisitRecordById(id), HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while fetching the visit record", null, false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<VisitRecordDto>> updateVisitRecord(@PathVariable Long id, @RequestBody VisitRecordDto visitRecordDto)
    {
        try {

            if (id == null || visitRecordDto == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Visit Record ID and data cannot be null");
            }
            return new ResponseEntity<>(visitRecordService.updateVisitRecord(id, visitRecordDto), HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while updating the visit record", null, false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<Void>> deleteVisitRecord(@PathVariable Long id){
        try {
            if (id == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Visit Record ID cannot be null");
            }
            return new ResponseEntity<>(visitRecordService.deleteVisitRecord(id), HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while deleting the visit record", null, false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #patientId == principal.id)")
    public ResponseEntity<ResponseTemplate<List<VisitRecordDto>>> getVisitRecordsByPatientId(@PathVariable Long patientId)
    {
        try {
            if (patientId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient ID cannot be null");
            }
            return new ResponseEntity<>(visitRecordService.getVisitRecordsByPatientId(patientId), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while fetching visit records for the patient", null, false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<ResponseTemplate<VisitRecordDto>> getVisitRecordByAppointmentId(@PathVariable Long appointmentId)
    {
        try {
            if (appointmentId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment ID cannot be null");
            }
            return new ResponseEntity<>(visitRecordService.getVisitRecordByAppointmentId(appointmentId), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while fetching the visit record for the appointment", null, false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<List<VisitRecordDto>>> getAllVisitRecords()
    {
        try {
            return new ResponseEntity<>(visitRecordService.getAllVisitRecords(), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while fetching all visit records", null, false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}