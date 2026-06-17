package com.example.DAR.DTO.In;

import com.example.DAR.Model.Home;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SensorDtoIn {


    @NotEmpty(message = "type is required")
    private String type;
    @NotEmpty(message = "location is required")
    private String location;







}
