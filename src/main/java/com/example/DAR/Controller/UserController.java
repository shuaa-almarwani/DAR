package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.UserDtoIn;
import com.example.DAR.Security.SecurityService;
import com.example.DAR.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;

    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDtoIn dto) {
        userService.addUser(dto);
        return ResponseEntity.status(200).body(new ApiResponse("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String token = userService.login(body.get("username"), body.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }

//    @PostMapping("/add")
//    public ResponseEntity<?> addUser(@RequestBody @Valid UserDtoIn dto) {
//        userService.addUser(dto);
//        return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
//    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDtoIn dto) {
        userService.updateUser(securityService.getCurrentUserId(), dto);
        return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteUser() {
        userService.deleteUser(securityService.getCurrentUserId());
        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserById() {
        return ResponseEntity.status(200).body(userService.getUserById(securityService.getCurrentUserId()));
    }
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.status(200).body(userService.getUserByEmail(email));
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.status(200).body(userService.getUserByUsername(username));

    }
    @PutMapping("/toggle-smart-alerts")
    public ResponseEntity<?> toggleSmartAlerts() {

        Boolean status = userService.toggleSmartAlerts(securityService.getCurrentUserId());

        if (status) {
            return ResponseEntity.status(200).body(new ApiResponse("Smart alerts enabled successfully"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Smart alerts disabled successfully"));
    }

}
