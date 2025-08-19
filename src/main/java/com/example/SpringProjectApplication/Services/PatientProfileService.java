package com.example.SpringProjectApplication.Services;

import com.example.SpringProjectApplication.Dtos.UserProfileDto;
import com.example.SpringProjectApplication.Response.ResponseTemplate;

import java.util.List;

public interface PatientProfileService
{
     ResponseTemplate<UserProfileDto> updateProfile(UserProfileDto userProfileDto);

     ResponseTemplate<List<UserProfileDto>> getAllPatients();

     ResponseTemplate<UserProfileDto> getProfileById(Long patientId);

     ResponseTemplate<Void> deletePatient(Long patientId);
}
