package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.MaintenanceDTOIn;
import com.example.DAR.Service.MaintenanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping("/get")
    public ResponseEntity<?> getMaintenances() {
        return ResponseEntity.status(200).body(maintenanceService.getAll());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getMaintenance(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(maintenanceService.getMaintenance(id));
    }

    @GetMapping("/get/home/{homeId}")
    public ResponseEntity<?> getMaintenancesByHome(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(maintenanceService.getMaintenancesByHome(homeId));
    }

    @GetMapping("/get/status/{status}")
    public ResponseEntity<?> getMaintenancesByStatus(@PathVariable String status) {
        return ResponseEntity.status(200).body(maintenanceService.getMaintenancesByStatus(status));
    }

    @GetMapping("/get/priority/{priority}")
    public ResponseEntity<?> getMaintenancesByPriority(@PathVariable String priority) {
        return ResponseEntity.status(200).body(maintenanceService.getMaintenancesByPriority(priority));
    }

    @PostMapping("/add/{homeId}/{homeItemId}")
    public ResponseEntity<?> addMaintenance(@PathVariable Integer homeId, @PathVariable Integer homeItemId, @RequestBody @Valid MaintenanceDTOIn maintenanceDTOIn) {
        maintenanceService.addMaintenance(homeId, homeItemId, maintenanceDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance added successfully"));
    }

    @PutMapping("/update/{id}/{homeId}/{homeItemId}")
    public ResponseEntity<?> updateMaintenance(@PathVariable Integer id, @PathVariable Integer homeId, @PathVariable Integer homeItemId, @RequestBody @Valid MaintenanceDTOIn maintenanceDTOIn) {
        maintenanceService.updateMaintenance(id, homeId, homeItemId, maintenanceDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMaintenance(@PathVariable Integer id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.status(200).body(new ApiResponse("Maintenance deleted successfully"));
    }
}
