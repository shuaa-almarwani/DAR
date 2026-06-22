package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.SensorDtoIn;
import com.example.DAR.Service.SensorService;
import com.example.DAR.Service.WorkflowTriggerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sensor")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;
    private  final WorkflowTriggerService workflowTriggerService ;

    @PostMapping("/add/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> addSensor(@PathVariable Integer homeId, @RequestBody @Valid SensorDtoIn dto) {
        sensorService.addSensor(homeId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Sensor added successfully"));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserSensor(#id)")
    public ResponseEntity<?> updateSensor(@PathVariable Integer id, @RequestBody @Valid SensorDtoIn dto) {
        sensorService.updateSensor(id, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Sensor updated successfully"));
    }

    @PutMapping("/connect/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserSensor(#id)")
    public ResponseEntity<?> sensorConnection(@PathVariable Integer id) {
        workflowTriggerService.connectSensor(id);
        sensorService.toggleActive(id);
        return ResponseEntity.status(200).body(new ApiResponse("Sensor has been connected"));
    }
    @PutMapping("/disconnect/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserSensor(#id)")
    public ResponseEntity<?> sensorDisconnect(@PathVariable Integer id) {
       // workflowTriggerService.connectSensor(id);
        sensorService.toggleDeactivate(id);
        return ResponseEntity.status(200).body(new ApiResponse("Sensor has been disconnect"));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserSensor(#id)")
    public ResponseEntity<?> deleteSensor(@PathVariable Integer id) {
        sensorService.deleteSensor(id);
        return ResponseEntity.status(200).body(new ApiResponse("Sensor deleted successfully"));
    }

    @GetMapping("/get/home/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getAllSensorsByHome(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(sensorService.getAllSensorsByHome(homeId));
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserSensor(#id)")
    public ResponseEntity<?> getSensorById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(sensorService.getSensorById(id));
    }

    @GetMapping("/get/active/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getActiveSensors(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(sensorService.getActiveSensors(homeId));
    }
}
