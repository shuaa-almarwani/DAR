package com.example.DAR.DTO.Out;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class PurchaseInvoiceDtoOut {

    private Integer id;
    private String productName;
    private String store;
    private Double amount;
    private LocalDate purchaseDate;
    private String category;
    private String imageUrl;
    private String warrantyNote;
    private LocalDate warrantyExpiry;




}
