package com.example.SpringProjectApplication.Repositories;

import com.example.SpringProjectApplication.Entities.VisitRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRecordRepository
        extends JpaRepository<VisitRecord, Long>
{
}
