package com.example.SpringProjectApplication.Repositories;

import com.example.SpringProjectApplication.Entities.Appointment;
import com.example.SpringProjectApplication.Entities.AppointmentType;
import com.example.SpringProjectApplication.Entities.User;
import com.example.SpringProjectApplication.Entities.VisitRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long>
{
    List<Appointment> findByPatient(User patient);

    Optional<Appointment> findByVisitRecord(VisitRecord visitRecord);
    List<Appointment> findByType(AppointmentType type);
}
