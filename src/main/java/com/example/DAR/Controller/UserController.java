package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.UserDtoIn;
import com.example.DAR.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
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

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDtoIn dto) {
        userService.addUser(dto);
        return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id,
                                     @RequestBody @Valid UserDtoIn dto) {
        userService.updateUser(id, dto);
        return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(userService.getUserById(id));
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.status(200).body(userService.getUserByEmail(email));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.status(200).body(userService.getUserByUsername(username));

    }
    @PutMapping("/toggle-smart-alerts/{userId}")
    public ResponseEntity<?> toggleSmartAlerts(@PathVariable Integer userId) {

        Boolean status = userService.toggleSmartAlerts(userId);

        if (status) {
            return ResponseEntity.status(200).body(new ApiResponse("Smart alerts enabled successfully"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Smart alerts disabled successfully"));
    }

}
