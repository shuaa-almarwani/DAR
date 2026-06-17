package com.example.DAR.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceReminderSummaryDTOOut {

    private Integer totalReminders;
    private Integer upcomingReminders;
    private Integer todayReminders;
    private Integer sentReminders;
    private Integer unsentReminders;
}
