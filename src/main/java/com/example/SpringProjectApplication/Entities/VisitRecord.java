package com.example.SpringProjectApplication.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Enumerated(EnumType.STRING)
    private VisitType type;

    private String comment;

    @Lob
    private String prescription;

    @CreationTimestamp
    private Timestamp createdAt;

    @OneToOne(mappedBy = "visitRecord")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;
}
