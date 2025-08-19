package com.example.SpringProjectApplication.Controllers;

import com.example.SpringProjectApplication.Dtos.UserProfileDto;
import com.example.SpringProjectApplication.Response.ResponseTemplate;
import com.example.SpringProjectApplication.Services.PatientProfileService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@AllArgsConstructor
public class PatientProfileController
{
    PatientProfileService patientProfileService;

    @PutMapping("/profile")
    public ResponseEntity<ResponseTemplate<UserProfileDto>> updateProfile(@Valid @RequestBody UserProfileDto userProfileDto)
    {
        try {
            return new ResponseEntity<>(patientProfileService.updateProfile(userProfileDto), HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while updating the profile", null, false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<List<UserProfileDto>>> getAllPatients()
    {
        try {
            return new ResponseEntity<>(patientProfileService.getAllPatients(), HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while fetching all patients", null, false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/profile/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #patientId == principal.id)")
    public ResponseEntity<ResponseTemplate<UserProfileDto>> getprofileById(@PathVariable Long patientId)
    {
        try {
            if (patientId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient ID cannot be null");
            }
            return new ResponseEntity<>(patientProfileService.getProfileById(patientId), HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while fetching the patient profile", null, false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/delete/{patientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<Void>> deletePatient(@PathVariable Long patientId)
    {
        try {
            if (patientId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient ID cannot be null");
            }
            return new ResponseEntity<>(patientProfileService.deletePatient(patientId), HttpStatus.NO_CONTENT);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ResponseTemplate<>(e.getReason(), null, false), e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseTemplate<>("An error occurred while deleting the patient", null, false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
