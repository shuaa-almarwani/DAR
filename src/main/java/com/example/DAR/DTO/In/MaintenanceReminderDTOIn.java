package com.example.DAR.DTO.In;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MaintenanceReminderDTOIn {

    @NotEmpty(message = "Title must not be empty")
    private String title;

    @NotEmpty(message = "Message must not be empty")
    private String message;

    @NotNull(message = "Reminder date must not be null")
    private LocalDate reminderDate;

    @NotEmpty(message = "Season must not be empty")
    private String season;

    @NotEmpty(message = "Weather condition must not be empty")
    private String weatherCondition;

    @NotEmpty(message = "Notification method cannot be empty")
    @Pattern(
            regexp = "EMAIL|WHATSAPP|CALL",
            message = "Notification method must be EMAIL, WHATSAPP, CALL"
    )
    private String notificationMethod;
}
