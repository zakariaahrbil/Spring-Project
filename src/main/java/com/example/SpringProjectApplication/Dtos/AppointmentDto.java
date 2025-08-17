package com.example.SpringProjectApplication.Dtos;

import com.example.SpringProjectApplication.Entities.AppointmentType;
import com.example.SpringProjectApplication.Entities.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AppointmentDto
{


    private Status status;

    @NotNull
    private AppointmentType type;

    @NotNull
    private LocalDateTime proposedDateTime;

    private LocalDateTime confirmedDateTime;

    @NotBlank
    @Size(max = 255)
    private String reason;

    @PastOrPresent
    private Timestamp createdAt;

    @Size(max = 255)
    private String meetingLink;

    private LocalDateTime startTime;

    @NotNull
    private Long patientId;

    private Long visitRecordId;
}
