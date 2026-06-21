package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.Enums.UserSubscriptionStatus;
import com.example.DAR.Service.UserSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/user-subscription")
@RestController
@RequiredArgsConstructor
public class UserSubscriptionController {
    private final UserSubscriptionService userSubscriptionService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllUserSubscriptions() {
        return ResponseEntity.status(200).body(userSubscriptionService.getAllUserSubscriptions());
    }

    @PostMapping("/subscribe/{userId}/{planId}")
    public ResponseEntity<?> subscribe(@PathVariable Integer userId, @PathVariable Integer planId) {
        return ResponseEntity.status(200).body(userSubscriptionService.createUserSubscription(userId, planId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserSubscription(@PathVariable Integer id) {
        userSubscriptionService.deleteUserSubscription(id);
        return ResponseEntity.status(200).body(new ApiResponse("User subscription deleted successfully"));
    }


    @PostMapping("/upgrade/{userId}/{planId}")
    public ResponseEntity<?> upgrade(@PathVariable Integer userId, @PathVariable Integer planId) {
        return ResponseEntity.status(200).body(userSubscriptionService.upgradeUserSubscription(userId, planId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserSubscriptionsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(userSubscriptionService.getAllUserSubscriptionsByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getUserSubscriptionsByStatus(@PathVariable UserSubscriptionStatus status) {
        return ResponseEntity.status(200).body(userSubscriptionService.getAllUserSubscriptionsByStatus(status));
    }
}
