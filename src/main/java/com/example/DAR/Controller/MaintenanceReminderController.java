package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.MaintenanceReminderDTOIn;
import com.example.DAR.Service.MaintenanceReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/maintenance-reminder")
@RequiredArgsConstructor
public class MaintenanceReminderController {

    private final MaintenanceReminderService maintenanceReminderService;

    @GetMapping("/get")
    public ResponseEntity<?> getMaintenanceReminders() {
        return ResponseEntity.status(200).body(maintenanceReminderService.getAll());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getMaintenanceReminder(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(maintenanceReminderService.getMaintenanceReminder(id));
    }

    @GetMapping("/get/home/{homeId}")
    public ResponseEntity<?> getRemindersByHome(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(maintenanceReminderService.getRemindersByHome(homeId));
    }

    @GetMapping("/get/unsent")
    public ResponseEntity<?> getUnsentReminders() {
        return ResponseEntity.status(200).body(maintenanceReminderService.getUnsentReminders());
    }

    @GetMapping("/get/season/{season}")
    public ResponseEntity<?> getRemindersBySeason(@PathVariable String season) {
        return ResponseEntity.status(200).body(maintenanceReminderService.getRemindersBySeason(season));
    }

    @PostMapping("/add/{homeId}/{homeItemId}")
    public ResponseEntity<?> addMaintenanceReminder(@PathVariable Integer homeId, @PathVariable Integer homeItemId, @RequestBody @Valid MaintenanceReminderDTOIn maintenanceReminderDTOIn) {
        maintenanceReminderService.addMaintenanceReminder(homeId, homeItemId, maintenanceReminderDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance reminder added successfully"));
    }

    @PutMapping("/update/{id}/{homeId}/{homeItemId}")
    public ResponseEntity<?> updateMaintenanceReminder(@PathVariable Integer id, @PathVariable Integer homeId, @PathVariable Integer homeItemId, @RequestBody @Valid MaintenanceReminderDTOIn maintenanceReminderDTOIn) {
        maintenanceReminderService.updateMaintenanceReminder(id, homeId, homeItemId, maintenanceReminderDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance reminder updated successfully"));
    }

    @PutMapping("/mark-sent/{id}")
    public ResponseEntity<?> markAsSent(@PathVariable Integer id) {
        maintenanceReminderService.markAsSent(id);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance reminder marked as sent"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMaintenanceReminder(@PathVariable Integer id) {
        maintenanceReminderService.deleteMaintenanceReminder(id);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance reminder deleted successfully"));
    }

    @GetMapping("/upcoming/{homeId}")
    public ResponseEntity<?> getUpcomingReminders(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(maintenanceReminderService.getUpcomingReminders(homeId));
    }

    @GetMapping("/today/{homeId}")
    public ResponseEntity<?> getTodayReminders(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(maintenanceReminderService.getTodayReminders(homeId));
    }

    @PostMapping("/send/{reminderId}")
    public ResponseEntity<?> sendReminder(@PathVariable Integer reminderId) {
        maintenanceReminderService.sendReminder(reminderId);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance reminder sent successfully"));
    }

    @PutMapping("/reactivate/{reminderId}")
    public ResponseEntity<?> reactivateReminder(@PathVariable Integer reminderId) {
        maintenanceReminderService.reactivateReminder(reminderId);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance reminder reactivated successfully"));
    }

    @GetMapping("/summary/{homeId}")
    public ResponseEntity<?> getReminderSummary(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(maintenanceReminderService.getReminderSummary(homeId));
    }
}
