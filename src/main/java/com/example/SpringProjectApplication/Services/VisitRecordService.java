package com.example.SpringProjectApplication.Services;

import com.example.SpringProjectApplication.Dtos.VisitRecordDto;
import com.example.SpringProjectApplication.Response.ResponseTemplate;

import java.util.List;

public interface VisitRecordService
{

    ResponseTemplate<VisitRecordDto> createVisitRecord(VisitRecordDto visitRecordDto);

    ResponseTemplate<VisitRecordDto> getVisitRecordById(Long id);

    ResponseTemplate<VisitRecordDto> updateVisitRecord(Long id, VisitRecordDto visitRecordDto);

    ResponseTemplate<Void> deleteVisitRecord(Long id);

    ResponseTemplate<List<VisitRecordDto>> getVisitRecordsByPatientId(Long patientId);

    ResponseTemplate<VisitRecordDto> getVisitRecordByAppointmentId(Long appointmentId);

    ResponseTemplate<List<VisitRecordDto>> getAllVisitRecords();

}
