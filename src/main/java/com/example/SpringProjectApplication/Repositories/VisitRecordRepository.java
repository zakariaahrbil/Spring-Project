package com.example.SpringProjectApplication.Repositories;

import com.example.SpringProjectApplication.Entities.Appointment;
import com.example.SpringProjectApplication.Entities.User;
import com.example.SpringProjectApplication.Entities.VisitRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitRecordRepository
        extends JpaRepository<VisitRecord, Long>
{
    List<VisitRecord> findByPatient(User user);

    VisitRecord findByAppointment(Appointment appointment);
}
