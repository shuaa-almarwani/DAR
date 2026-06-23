package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.MaintenanceDTOIn;
import com.example.DAR.Service.MaintenanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getMaintenances() {
        return ResponseEntity.status(200).body(maintenanceService.getAll());
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserMaintenance(#id)")
    public ResponseEntity<?> getMaintenance(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(maintenanceService.getMaintenance(id));
    }

    @GetMapping("/get/home/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getMaintenancesByHome(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(maintenanceService.getMaintenancesByHome(homeId));
    }

    @GetMapping("/get/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getMaintenancesByStatus(@PathVariable String status) {
        return ResponseEntity.status(200).body(maintenanceService.getMaintenancesByStatus(status));
    }

    @GetMapping("/get/priority/{priority}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getMaintenancesByPriority(@PathVariable String priority) {
        return ResponseEntity.status(200).body(maintenanceService.getMaintenancesByPriority(priority));
    }

    @PostMapping("/add/{homeId}/{homeItemId}")
    @PreAuthorize("hasRole('ADMIN') or (@securityService.isCurrentUserHome(#homeId) and @securityService.isCurrentUserHomeItem(#homeItemId))")
    public ResponseEntity<?> addMaintenance(@PathVariable Integer homeId, @PathVariable Integer homeItemId, @RequestBody @Valid MaintenanceDTOIn maintenanceDTOIn) {
        maintenanceService.addMaintenance(homeId, homeItemId, maintenanceDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance added successfully"));
    }

    @PutMapping("/update/{id}/{homeId}/{homeItemId}")
    @PreAuthorize("hasRole('ADMIN') or (@securityService.isCurrentUserMaintenance(#id) and @securityService.isCurrentUserHome(#homeId) and @securityService.isCurrentUserHomeItem(#homeItemId))")
    public ResponseEntity<?> updateMaintenance(@PathVariable Integer id, @PathVariable Integer homeId, @PathVariable Integer homeItemId, @RequestBody @Valid MaintenanceDTOIn maintenanceDTOIn) {
        maintenanceService.updateMaintenance(id, homeId, homeItemId, maintenanceDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserMaintenance(#id)")
    public ResponseEntity<?> deleteMaintenance(@PathVariable Integer id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance deleted successfully"));
    }
    @GetMapping("/upcoming/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getUpcomingMaintenances(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(maintenanceService.getUpcomingMaintenances(homeId));
    }

    @GetMapping("/overdue/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getOverdueMaintenances(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(maintenanceService.getOverdueMaintenances(homeId));
    }

    @PutMapping("/mark-done/{maintenanceId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserMaintenance(#maintenanceId)")
    public ResponseEntity<?> markMaintenanceAsDone(@PathVariable Integer maintenanceId) {
        maintenanceService.markMaintenanceAsDone(maintenanceId);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance marked as done successfully"));
    }

    @GetMapping("/summary/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getMaintenanceSummary(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(maintenanceService.getMaintenanceSummary(homeId));
    }


}
