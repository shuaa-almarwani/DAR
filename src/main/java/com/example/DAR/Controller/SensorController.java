package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.SensorDtoIn;
import com.example.DAR.Service.SensorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sensor")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @PostMapping("/add/{homeId}")
    public ResponseEntity<?> addSensor(@PathVariable Integer homeId, @RequestBody @Valid SensorDtoIn dto) {
        sensorService.addSensor(homeId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Sensor added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSensor(@PathVariable Integer id, @RequestBody @Valid SensorDtoIn dto) {
        sensorService.updateSensor(id, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Sensor updated successfully"));
    }

    @PutMapping("/toggle/{id}")
    public ResponseEntity<?> toggleActive(@PathVariable Integer id) {
        sensorService.toggleActive(id);
        return ResponseEntity.status(200).body(new ApiResponse("Sensor status toggled"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSensor(@PathVariable Integer id) {
        sensorService.deleteSensor(id);
        return ResponseEntity.status(200).body(new ApiResponse("Sensor deleted successfully"));
    }

    @GetMapping("/get/home/{homeId}")
    public ResponseEntity<?> getAllSensorsByHome(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(sensorService.getAllSensorsByHome(homeId));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getSensorById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(sensorService.getSensorById(id));
    }

    @GetMapping("/get/active/{homeId}")
    public ResponseEntity<?> getActiveSensors(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(sensorService.getActiveSensors(homeId));
    }
}
