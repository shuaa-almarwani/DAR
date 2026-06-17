package com.example.DAR.DTO.Out;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HomeItemDTOOut {

    private Integer id;
    private String category;
    private String brand;
    private LocalDate installDate;
    private Integer lifespanMonth;
    private LocalDate nextServiceDate;
    private String notes;
    private String homeAddress;  // from Home.address
}
