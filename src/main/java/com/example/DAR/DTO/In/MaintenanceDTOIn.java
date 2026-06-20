package com.example.DAR.DTO.In;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MaintenanceDTOIn {

    @NotEmpty(message = "Title must not be empty")
    private String title;

    @NotEmpty(message = "Description must not be empty")
    private String description;

    @NotNull(message = "Maintenance date must not be null")
    private LocalDate maintenanceDate;

    @NotNull(message = "Cost must not be null")
    @Positive(message = "Cost must be a positive number")
    private Double cost;

    @Pattern(
            regexp = "^(SCHEDULED|IN_PROGRESS|DONE|CANCELLED)$",
            message = "Status must be SCHEDULED, IN_PROGRESS, DONE, or CANCELLED"
    )
    @NotEmpty(message = "Status must not be empty")
    private String status;

    @NotEmpty(message = "Priority must not be empty")
    @Pattern(regexp = "LOW|MEDIUM|HIGH|URGENT", message = "Priority must be LOW, MEDIUM, HIGH, or URGENT")
    private String priority;

    @NotEmpty(message = "Notes must not be empty")
    private String notes;

}
