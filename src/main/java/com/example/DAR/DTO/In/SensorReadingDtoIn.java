package com.example.DAR.DTO.In;

import com.example.DAR.Model.Sensor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorReadingDtoIn {


    @NotNull(message = "readingValue is required")
    private Double readingValue;
    @NotEmpty(message = "unit is required")
    private String unit;
    @NotNull(message = "readingDate is required")
    private LocalDateTime readingDate;


}
