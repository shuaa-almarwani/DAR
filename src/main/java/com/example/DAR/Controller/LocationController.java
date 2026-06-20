package com.example.DAR.Controller;

import com.example.DAR.Service.NominatimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
public class LocationController {

    private final NominatimService nominatimService;

    @GetMapping("/geocode")
    public ResponseEntity<?> geocode(@RequestParam String address) {
        return ResponseEntity.status(200).body(nominatimService.geocode(address));
    }
}
