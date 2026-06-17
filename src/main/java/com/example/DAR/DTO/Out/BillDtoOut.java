package com.example.DAR.DTO.Out;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BillDtoOut {

    private String type;
    private LocalDate billMonth;
    private Integer consumption;
    private String unit;
    private Boolean isInstallment;
    private Integer totalInstallment;
    private Integer paidInstallment;
    private String status;
    private String imageUrl;


}
