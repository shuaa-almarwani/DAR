package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.Enums.UserSubscriptionStatus;
import com.example.DAR.Security.SecurityService;
import com.example.DAR.Service.UserSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/user-subscription")
@RestController
@RequiredArgsConstructor
public class UserSubscriptionController {
    private final UserSubscriptionService userSubscriptionService;
    private final SecurityService securityService;

    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUserSubscriptions() {
        return ResponseEntity.status(200).body(userSubscriptionService.getAllUserSubscriptions());
    }

    @PostMapping("/subscribe/{planId}")
    public ResponseEntity<?> subscribe(@PathVariable Integer planId) {
        return ResponseEntity.status(200).body(userSubscriptionService.createUserSubscription(securityService.getCurrentUserId(), planId));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserSubscription(@PathVariable Integer id) {
        userSubscriptionService.deleteUserSubscription(id);
        return ResponseEntity.status(200).body(new ApiResponse("User subscription deleted successfully"));
    }


    @PostMapping("/upgrade/{planId}")
    public ResponseEntity<?> upgrade(@PathVariable Integer planId) {
        return ResponseEntity.status(200).body(userSubscriptionService.upgradeUserSubscription(securityService.getCurrentUserId(), planId));
    }

    @GetMapping("/user-subscriptions")
    public ResponseEntity<?> getUserSubscriptionsByUserId() {
        return ResponseEntity.status(200).body(userSubscriptionService.getAllUserSubscriptionsByUserId(securityService.getCurrentUserId()));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserSubscriptionsByStatus(@PathVariable UserSubscriptionStatus status) {
        return ResponseEntity.status(200).body(userSubscriptionService.getAllUserSubscriptionsByStatus(status));
    }
}
