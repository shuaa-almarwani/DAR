package com.example.DAR.DTO.Out;

import com.example.DAR.Model.Home;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SensorDtoOut {


    private Integer id;
    private String type;
    private String location;
    private Boolean isActive;
    private LocalDate lastPing;





}
