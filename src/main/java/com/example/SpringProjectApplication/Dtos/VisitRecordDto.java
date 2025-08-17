package com.example.SpringProjectApplication.Dtos;

import com.example.SpringProjectApplication.Entities.VisitType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VisitRecordDto {

    @NotNull
    private VisitType type;

    private String comment;

    private String prescription;

    @NotNull
    private Long appointmentId;
}
