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

public class PurchaseInvoiceDtoIn {


    @NotEmpty(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String productName;

    @NotEmpty(message = "Store is required")
    @Size(min = 2, max = 100, message = "Store name must be between 2 and 100 characters")
    private String store;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private Double amount;

    @NotNull(message = "Purchase date is required")
    @PastOrPresent(message = "Purchase date cannot be in the future")
    private LocalDate purchaseDate;

    @NotEmpty(message = "Category is required")
    private String category;

    @Pattern(regexp = "https?://.*", message = "Image URL must be a valid URL")
    private String imageUrl;

    private String warrantyNote;

    @Future(message = "Warranty expiry must be a future date")
    private LocalDate warrantyExpiry;




}
