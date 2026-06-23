package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.MaintenanceReminderDTOIn;
import com.example.DAR.Service.MaintenanceReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/maintenance-reminder")
@RequiredArgsConstructor
public class MaintenanceReminderController {

    private final MaintenanceReminderService maintenanceReminderService;

    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getMaintenanceReminders() {
        return ResponseEntity.status(200).body(maintenanceReminderService.getAll());
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserReminder(#id)")
    public ResponseEntity<?> getMaintenanceReminder(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(maintenanceReminderService.getMaintenanceReminder(id));
    }

    @GetMapping("/get/home/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getRemindersByHome(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(maintenanceReminderService.getRemindersByHome(homeId));
    }

    @GetMapping("/get/unsent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUnsentReminders() {
        return ResponseEntity.status(200).body(maintenanceReminderService.getUnsentReminders());
    }

    @GetMapping("/get/season/{season}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getRemindersBySeason(@PathVariable String season) {
        return ResponseEntity.status(200).body(maintenanceReminderService.getRemindersBySeason(season));
    }

    @PostMapping("/add/{maintenanceId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserMaintenance(#maintenanceId)")
    public ResponseEntity<?> addMaintenanceReminder(@PathVariable Integer maintenanceId,
                                                    @RequestBody @Valid MaintenanceReminderDTOIn dto) {
        maintenanceReminderService.addMaintenanceReminder(maintenanceId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance reminder added successfully"));
    }

    @PutMapping("/update/{id}/{homeId}/{homeItemId}")
    @PreAuthorize("hasRole('ADMIN') or (@securityService.isCurrentUserReminder(#id) and @securityService.isCurrentUserHome(#homeId) and @securityService.isCurrentUserHomeItem(#homeItemId))")
    public ResponseEntity<?> updateMaintenanceReminder(@PathVariable Integer id, @PathVariable Integer homeId, @PathVariable Integer homeItemId, @RequestBody @Valid MaintenanceReminderDTOIn maintenanceReminderDTOIn) {
        maintenanceReminderService.updateMaintenanceReminder(id, homeId, homeItemId, maintenanceReminderDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance reminder updated successfully"));
    }

    @PutMapping("/mark-sent/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserReminder(#id)")
    public ResponseEntity<?> markAsSent(@PathVariable Integer id) {
        maintenanceReminderService.markAsSent(id);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance reminder marked as sent"));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserReminder(#id)")
    public ResponseEntity<?> deleteMaintenanceReminder(@PathVariable Integer id) {
        maintenanceReminderService.deleteMaintenanceReminder(id);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance reminder deleted successfully"));
    }

    @GetMapping("/upcoming/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getUpcomingReminders(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(maintenanceReminderService.getUpcomingReminders(homeId));
    }

    @GetMapping("/today/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getTodayReminders(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(maintenanceReminderService.getTodayReminders(homeId));
    }

    @PostMapping("/send/{reminderId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserReminder(#reminderId)")
    public ResponseEntity<?> sendReminder(@PathVariable Integer reminderId) {
        maintenanceReminderService.sendReminder(reminderId);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance reminder sent successfully"));
    }

    @PutMapping("/reactivate/{reminderId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserReminder(#reminderId)")
    public ResponseEntity<?> reactivateReminder(@PathVariable Integer reminderId) {
        maintenanceReminderService.reactivateReminder(reminderId);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance reminder reactivated successfully"));
    }

    @GetMapping("/summary/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getReminderSummary(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(maintenanceReminderService.getReminderSummary(homeId));
    }

    @GetMapping("/ai-weather-advice/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getAIWeatherMaintenanceAdvice(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(new ApiResponse(maintenanceReminderService.getAIWeatherMaintenanceAdvice(homeId)));
    }

}
