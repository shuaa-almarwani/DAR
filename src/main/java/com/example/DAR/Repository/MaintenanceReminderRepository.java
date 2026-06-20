package com.example.DAR.Repository;

import com.example.DAR.Model.MaintenanceReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceReminderRepository extends JpaRepository<MaintenanceReminder, Integer> {

    MaintenanceReminder findMaintenanceReminderById(Integer id);

    List<MaintenanceReminder> findMaintenanceRemindersByHomeId(Integer homeId);

    List<MaintenanceReminder> findMaintenanceRemindersByIsSent(Boolean isSent);

    List<MaintenanceReminder> findMaintenanceRemindersBySeason(String season);

    List<MaintenanceReminder> findMaintenanceRemindersByMaintenanceId(Integer maintenanceId);
    List<MaintenanceReminder> findMaintenanceRemindersByReminderDateAndIsSent(
            LocalDate reminderDate,
            Boolean isSent
    );
}
