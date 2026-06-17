package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.SensorReadingDtoIn;
import com.example.DAR.Service.Sensorreadingservice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sensor-reading")
@RequiredArgsConstructor
public class SensorReadingController {

    private final Sensorreadingservice sensorReadingService;

    @PostMapping("/add/{sensorId}")
    public ResponseEntity<?> addReading(@PathVariable Integer sensorId, @RequestBody @Valid SensorReadingDtoIn dto) {
        sensorReadingService.addReading(sensorId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Reading added successfully"));
    }

    @GetMapping("/get/sensor/{sensorId}")
    public ResponseEntity<?> getAllReadingsBySensor(@PathVariable Integer sensorId) {
        return ResponseEntity.status(200).body(sensorReadingService.getAllReadingsBySensor(sensorId));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getReadingById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(sensorReadingService.getReadingById(id));
    }

    @GetMapping("/get/latest/{sensorId}")
    public ResponseEntity<?> getLatestReading(@PathVariable Integer sensorId) {
        return ResponseEntity.status(200).body(sensorReadingService.getLatestReading(sensorId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReading(@PathVariable Integer id) {
        sensorReadingService.deleteReading(id);
        return ResponseEntity.status(200).body(new ApiResponse("Reading deleted successfully"));
    }
}
