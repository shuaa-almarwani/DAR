package com.example.DAR.DTO.In;

import com.example.DAR.Enums.HomeItemCategory;
import com.example.DAR.Enums.HomeItemStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HomeItemDTOIn {

    @NotEmpty(message = "Name must not be empty")
    private String name;

    @NotNull(message = "Category must not be null")
    private HomeItemCategory category;

    private String customCategory;

    @NotEmpty(message = "Brand must not be empty")
    private String brand;

    private String model;

    @NotEmpty(message = "Location must not be empty")
    private String location;

    @NotNull(message = "Install date must not be null")
    private LocalDate installDate;

    private LocalDate purchaseDate;

    @NotNull(message = "Lifespan in months must not be null")
    @Positive(message = "Lifespan must be a positive number")
    private Integer lifespanMonth;

    @Positive(message = "Warranty months must be a positive number")
    private Integer warrantyMonths;

    @NotNull(message = "Next service date must not be null")
    private LocalDate nextServiceDate;

    private String imageUrl;

    @NotNull(message = "Status must not be null")
    private HomeItemStatus status;

    private String notes;

}
