package com.example.DAR.DTO.Out;

import com.example.DAR.Enums.HomeItemCategory;
import com.example.DAR.Enums.HomeItemStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HomeItemDTOOut {

    private Integer id;
    private String name;
    private HomeItemCategory category;
    private String customCategory;
    private String brand;
    private String model;
    private String location;
    private LocalDate installDate;
    private LocalDate purchaseDate;
    private Integer lifespanMonth;
    private Integer warrantyMonths;
    private LocalDate nextServiceDate;
    private String imageUrl;
    private HomeItemStatus status;
    private String notes;
    private String homeAddress;  // from Home.address
}
