package com.example.DAR.DTO.Out;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MaintenanceReminderDTOOut {

    private Integer id;
    private String title;
    private String message;
    private LocalDate reminderDate;
    private String season;
    private String weatherCondition;
    private Boolean isSent;
    private LocalDate createdAt;
    private String homeAddress;      // from Home.address
    private String homeItemCategory; // from HomeItem.category
    private String homeItemBrand;// from HomeItem.brand


    private Integer maintenanceId;
    private String maintenanceTitle;
    private String maintenancePriority;
    private String maintenanceStatus;

    private String notificationMethod;
}
