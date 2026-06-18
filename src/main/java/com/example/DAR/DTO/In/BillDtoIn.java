package com.example.DAR.DTO.In;

import com.example.DAR.Model.Home;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BillDtoIn {




    @NotNull(message = "Bill type is required")
    @Pattern( regexp = "(?i)^(WATER|ELECTRICITY|GAS)$",message = "Type must be WATER, ELECTRICITY, or GAS")
    private String type;

    @NotNull(message = "Bill month is required")
    @PastOrPresent(message = "Bill month cannot be in the future")
    private LocalDate billMonth;

    @NotNull(message = "Consumption is required")
    @Positive(message = "Consumption must be greater than zero")
    private Integer consumption;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private Double amount;

    @NotBlank(message = "Unit is required")
    private String unit;

    @NotNull(message = "isInstallment is required")
    private Boolean isInstallment;

    @Min(value = 0, message = "Total installments cannot be negative")
    private Integer totalInstallment;

    @Min(value = 0, message = "Paid installments cannot be negative")
    private Integer paidInstallment;

    @NotNull(message = "Status is required")
    private String status; // مدري مطلوب او لا في البدايه

    @Pattern(regexp = "https?://.*", message = "Image URL must be a valid URL")
    private String imageUrl;


}
