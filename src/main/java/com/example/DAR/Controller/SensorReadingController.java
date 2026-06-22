package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.SensorReadingDtoIn;
import com.example.DAR.Service.PdfReportService;
import com.example.DAR.Service.Sensorreadingservice;
import com.example.DAR.Service.WorkflowTriggerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sensor-reading")
@RequiredArgsConstructor
public class SensorReadingController {

    private final Sensorreadingservice sensorReadingService;
    private final WorkflowTriggerService workflowTriggerService;
    private final PdfReportService pdfReportService;



    @GetMapping("/get/sensor/{sensorId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserSensor(#sensorId)")
    public ResponseEntity<?> getAllReadingsBySensor(@PathVariable Integer sensorId) {
        return ResponseEntity.status(200).body(sensorReadingService.getAllReadingsBySensor(sensorId));
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserSensorReading(#id)")
    public ResponseEntity<?> getReadingById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(sensorReadingService.getReadingById(id));
    }

    @GetMapping("/get/latest/{sensorId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserSensor(#sensorId)")
    public ResponseEntity<?> getLatestReading(@PathVariable Integer sensorId) {
        return ResponseEntity.status(200).body(sensorReadingService.getLatestReading(sensorId));
    }



    @GetMapping("/analyze/{sensorId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserSensor(#sensorId)")
    public ResponseEntity<?> analyzeSensorReadings(@PathVariable Integer sensorId) {
        return ResponseEntity.status(200).body(workflowTriggerService.triggerSensorAnalysis(sensorId));
    }

    @GetMapping("/report/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getSensorReport(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(sensorReadingService.getSensorReport(homeId));
    }

    @GetMapping("/report/{homeId}/pdf")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<byte[]> getSensorReportPdf(@PathVariable Integer homeId) {
        byte[] pdf = pdfReportService.generateSensorReport(homeId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sensor-report-home-" + homeId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
