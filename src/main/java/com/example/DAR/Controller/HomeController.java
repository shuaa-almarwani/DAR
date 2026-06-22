package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.HomeDTOIn;
import com.example.DAR.Security.SecurityService;
import com.example.DAR.Service.HomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final SecurityService securityService;

    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getHomes() {
        return ResponseEntity.status(200).body(homeService.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addHome(@RequestBody @Valid HomeDTOIn homeDTOIn) {
        homeService.addHome(securityService.getCurrentUserId(), homeDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Home added successfully"));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#id)")
    public ResponseEntity<?> updateHome(@PathVariable Integer id, @RequestBody @Valid HomeDTOIn homeDTOIn) {
        homeService.updateHome(id, securityService.getCurrentUserId(), homeDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Home updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#id)")
    public ResponseEntity<?> deleteHome(@PathVariable Integer id) {
        homeService.deleteHome(id);
        return ResponseEntity.status(200).body(new ApiResponse("Home deleted successfully"));
    }


    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#id)")
    public ResponseEntity<?> getHome(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(homeService.getHome(id));
    }

    @GetMapping("/user-homes")
    public ResponseEntity<?> getHomesByUser() {
        return ResponseEntity.status(200).body(homeService.getHomesByUser(securityService.getCurrentUserId()));
    }
}
