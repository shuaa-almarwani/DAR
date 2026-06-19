package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.HomeDTOIn;
import com.example.DAR.Service.HomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/get")
    public ResponseEntity<?> getHomes() {
        return ResponseEntity.status(200).body(homeService.getAll());
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addHome(@PathVariable Integer userId, @RequestBody @Valid HomeDTOIn homeDTOIn) {
        homeService.addHome(userId, homeDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Home added successfully"));
    }

    @PutMapping("/update/{id}/{userId}")
    public ResponseEntity<?> updateHome(@PathVariable Integer id, @PathVariable Integer userId, @RequestBody @Valid HomeDTOIn homeDTOIn) {
        homeService.updateHome(id, userId, homeDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Home updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteHome(@PathVariable Integer id) {
        homeService.deleteHome(id);
        return ResponseEntity.status(200).body(new ApiResponse("Home deleted successfully"));
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<?> getHome(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(homeService.getHome(id));
    }

    @GetMapping("/get/user/{userId}")
    public ResponseEntity<?> getHomesByUser(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(homeService.getHomesByUser(userId));
    }
}
