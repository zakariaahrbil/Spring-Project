package com.example.SpringProjectApplication.Services;

import com.example.SpringProjectApplication.Dtos.UserProfileDto;
import com.example.SpringProjectApplication.Entities.Role;
import com.example.SpringProjectApplication.Entities.User;
import com.example.SpringProjectApplication.Mappers.UserProfileMapper;
import com.example.SpringProjectApplication.Repositories.UserRepository;
import com.example.SpringProjectApplication.Response.ResponseTemplate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PatientProfileServiceImpl
        implements PatientProfileService
{
    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    public ResponseTemplate<UserProfileDto> updateProfile(UserProfileDto userProfileDto)
    {
        try {
            Optional<User> optionalUser = userRepository.findById(userProfileDto.getId());
            if (optionalUser.isEmpty()) {
                throw new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "User not found with ID: " + userProfileDto.getId()
                );
            }

            User user = optionalUser.get();
            user.setFullName(userProfileDto.getFullName());
            user.setProfilePictureUrl(userProfileDto.getProfilePictureUrl());
            user.setPhoneNumber(userProfileDto.getPhoneNumber());
            user.setAddress(userProfileDto.getAddress());
            user.setGender(userProfileDto.getGender());
            user.setBirthday(userProfileDto.getBirthday());

            userRepository.save(user);
            UserProfileDto updatedUserProfileDto = userProfileMapper.toDto(user);
            return new ResponseTemplate<>("Profile updated successfully", updatedUserProfileDto, true);
        }
        catch (ResponseStatusException e) {
            throw e;
        }
        catch (Exception e) {
            return new ResponseTemplate<>("An error occurred while updating the profile", null, false);
        }
    }

    @Override
    public ResponseTemplate<List<UserProfileDto>> getAllPatients()
    {
        try {
            List<User> patients = userRepository.findAllByRole(Role.USER);
            List<UserProfileDto> patientDtos = userProfileMapper.toDto(patients);
            return new ResponseTemplate<>("Patients retrieved successfully", patientDtos, true);
        }
        catch (Exception e) {
            return new ResponseTemplate<>("An error occurred while retrieving patients", null, false);
        }
    }

    @Override
    public ResponseTemplate<UserProfileDto> getProfileById(Long patientId)
    {
        try {
            Optional<User> optionalUser = userRepository.findById(patientId);
            if (optionalUser.isEmpty()) {
                throw new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "User not found with ID: " + patientId
                );
            }
            User user = optionalUser.get();
            UserProfileDto userProfileDto = userProfileMapper.toDto(user);
            return new ResponseTemplate<>("Patient profile retrieved successfully", userProfileDto, true);
        }
        catch (ResponseStatusException e) {
            throw e;
        }
        catch (Exception e) {
            return new ResponseTemplate<>("An error occurred while retrieving the patient profile", null, false);
        }
    }

    @Override
    public ResponseTemplate<Void> deletePatient(Long patientId)
    {
        try {
            Optional<User> optionalUser = userRepository.findById(patientId);
            if (optionalUser.isEmpty()) {
                throw new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "User not found with ID: " + patientId
                );
            }
            userRepository.delete(optionalUser.get());
            return new ResponseTemplate<>("Patient deleted successfully", null, true);
        }
        catch (ResponseStatusException e) {
            throw e;
        }
        catch (Exception e) {
            return new ResponseTemplate<>("An error occurred while deleting the patient", null, false);
        }
    }
}
