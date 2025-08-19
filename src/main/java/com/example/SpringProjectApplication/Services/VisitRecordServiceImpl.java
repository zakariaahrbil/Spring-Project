package com.example.SpringProjectApplication.Services;

import com.example.SpringProjectApplication.Dtos.VisitRecordDto;
import com.example.SpringProjectApplication.Entities.Appointment;
import com.example.SpringProjectApplication.Entities.Role;
import com.example.SpringProjectApplication.Entities.Status;
import com.example.SpringProjectApplication.Entities.User;
import com.example.SpringProjectApplication.Entities.VisitRecord;
import com.example.SpringProjectApplication.Mappers.VisitRecordMapper;
import com.example.SpringProjectApplication.Repositories.AppointmentRepository;
import com.example.SpringProjectApplication.Repositories.UserRepository;
import com.example.SpringProjectApplication.Repositories.VisitRecordRepository;
import com.example.SpringProjectApplication.Response.ResponseTemplate;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VisitRecordServiceImpl
        implements VisitRecordService
{
    AppointmentRepository appointmentRepository;
    VisitRecordRepository visitRecordRepository;
    UserRepository userRepository;
    VisitRecordMapper visitRecordMapper;
    @Override
    public ResponseTemplate<VisitRecordDto> createVisitRecord(VisitRecordDto visitRecordDto)
    {
        try{

            Optional<Appointment> appointment = appointmentRepository.findById(visitRecordDto.getAppointmentId());
            if (appointment.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found with ID: " + visitRecordDto.getAppointmentId());
            }
            User patient = appointment.get().getPatient();
            if (patient == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found with ID: " + visitRecordDto.getPatientId());
            }
            Appointment appointmentEntity = appointment.get();
            appointmentEntity.setStatus(Status.COMPLETED);
            if (appointmentEntity.getVisitRecord() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Visit record already exists for this appointment");
            }
            VisitRecord visitRecord = visitRecordMapper.toEntity(visitRecordDto);
            visitRecord.setAppointment(appointment.get());
            visitRecord.setPatient(patient);
            visitRecord = visitRecordRepository.save(visitRecord);
            appointmentRepository.save(appointmentEntity);
            VisitRecordDto savedVisitRecordDto = visitRecordMapper.toDto(visitRecord);
            return new ResponseTemplate<>( "Visit record created successfully",savedVisitRecordDto, true);
        }
        catch(ResponseStatusException e){
            throw e;
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while creating the visit record",
                    e
            );
        }
    }

    @Override
    public ResponseTemplate<VisitRecordDto> getVisitRecordById(Long id)
    {
        try {
            Optional<VisitRecord> visitRecord = visitRecordRepository.findById(id);
            if (visitRecord.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Visit record not found with ID: " + id);
            }
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!visitRecord.get().getPatient().getId().equals(currentUser.getId())
                    && !currentUser.getRole().equals(Role.ADMIN)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this visit record");
            }
            VisitRecordDto visitRecordDto = visitRecordMapper.toDto(visitRecord.get());
            return new ResponseTemplate<>("Visit record fetched successfully", visitRecordDto, true);
        }catch (ResponseStatusException e){
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while fetching the visit record",
                    e
            );
        }
    }

    @Override
    public ResponseTemplate<VisitRecordDto> updateVisitRecord(Long id, VisitRecordDto visitRecordDto)
    {
        try {
            Optional<VisitRecord> existingVisitRecord = visitRecordRepository.findById(id);
            if (existingVisitRecord.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Visit record not found with ID: " + id);
            }
            VisitRecord visitRecord = existingVisitRecord.get();
            visitRecord.setType(visitRecordDto.getType());
            visitRecord.setComment(visitRecordDto.getComment());
            visitRecord.setPrescription(visitRecordDto.getPrescription());
            visitRecordRepository.save(visitRecord);
            VisitRecordDto updatedVisitRecordDto = visitRecordMapper.toDto(visitRecord);
            return new ResponseTemplate<>("Visit record updated successfully", updatedVisitRecordDto, true);

        }catch (ResponseStatusException e){
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while updating the visit record",
                    e
            );
        }
    }

    @Override
    public ResponseTemplate<Void> deleteVisitRecord(Long id)
    {
        try{
            Optional<VisitRecord> visitRecord = visitRecordRepository.findById(id);
            if (visitRecord.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Visit record not found with ID: " + id);
            }
            visitRecordRepository.delete(visitRecord.get());
            return new ResponseTemplate<>("Visit record deleted successfully", null, true);
        }
        catch (ResponseStatusException e){
            throw e;
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while deleting the visit record",
                    e
            );
        }
    }

    @Override
    public ResponseTemplate<List<VisitRecordDto>> getVisitRecordsByPatientId(Long patientId)
    {
        try{
            Optional<User> patient = userRepository.findById(patientId);
            if (patient.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found with ID: " + patientId);
            }
            List<VisitRecord> visitRecords = visitRecordRepository.findByPatient(patient.get());
            List<VisitRecordDto> visitRecordDtos = new ArrayList<>();
            for(VisitRecord visitRecord : visitRecords){
                visitRecordDtos.add(visitRecordMapper.toDto(visitRecord));
            }
            System.out.println("Visit Records: " + visitRecordDtos);

            return new ResponseTemplate<>("Visit records fetched successfully", visitRecordDtos, true);

        }
        catch(ResponseStatusException e){
            throw e;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while fetching visit records for the patient",
                    e
            );
        }
    }

    @Override
    public ResponseTemplate<VisitRecordDto> getVisitRecordByAppointmentId(Long appointmentId)
    {
        try {
            Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
            if (appointment.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found with ID: " + appointmentId);
            }
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!appointment.get().getPatient().getId().equals(currentUser.getId())
                    && !currentUser.getRole().equals(Role.ADMIN)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this visit record");
            }
            VisitRecord visitRecord = visitRecordRepository.findByAppointment(appointment.get());
            if (visitRecord == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Visit record not found for appointment ID: " + appointmentId);
            }
            VisitRecordDto visitRecordDto = visitRecordMapper.toDto(visitRecord);
            return new ResponseTemplate<>("Visit record fetched successfully", visitRecordDto, true);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while fetching the visit record for the appointment",
                    e
            );
        }
    }

    @Override
    public ResponseTemplate<List<VisitRecordDto>> getAllVisitRecords()
    {
        try{
            List<VisitRecord> visitRecords = visitRecordRepository.findAll();
            List<VisitRecordDto> visitRecordDtos = visitRecords.stream().map(visitRecordMapper::toDto)
                    .toList();
            return new ResponseTemplate<>("All visit records fetched successfully", visitRecordDtos, true);
        }
        catch(ResponseStatusException e){
            throw e;
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while fetching all visit records",
                    e
            );
        }
    }
}
