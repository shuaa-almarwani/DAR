package com.example.DAR.DTO.In;

import com.example.DAR.Enums.HomePropertyType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class HomeDTOIn {

    @NotEmpty(message = "Address must not be empty")
    private String address;

    @NotEmpty
    private String city;

    @NotNull(message = "Latitude must not be null")
    private Double latitude;

    @NotNull(message = "Longitude must not be null")
    private Double longitude;

    @NotNull(message = "Build year must not be null")
    @Positive(message = "Build year must be a positive number")
    private Integer buildYear;

    @NotNull(message = "Property type must not be null")
    private HomePropertyType propertyType;

}
