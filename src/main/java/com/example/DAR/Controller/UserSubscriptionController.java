package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.UserSubscriptionDtoIn;
import com.example.DAR.Repository.UserSubscriptionRepository;
import com.example.DAR.Service.UserSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/user-subscription")
@RestController
@RequiredArgsConstructor
public class UserSubscriptionController {
    private final UserSubscriptionService userSubscriptionService;

    @GetMapping("/get")
    public ResponseEntity getAllUserSubscriptions() {
        return ResponseEntity.status(200).body(userSubscriptionService.getAllUserSubscriptions());
    }

    @PostMapping("/add/{userId}/{planId}")
    public ResponseEntity addUserSubscription(@PathVariable Integer userId,
                                              @PathVariable Integer planId,
                                              @RequestBody @Valid UserSubscriptionDtoIn dto) {
        userSubscriptionService.addUserSubscription(userId, planId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("User subscription added successfully"));
    }

    @PutMapping("/update/{subscriptionId}/{userId}/{planId}")
    public ResponseEntity updateUserSubscription(@PathVariable Integer subscriptionId,
                                                 @PathVariable Integer userId,
                                                 @PathVariable Integer planId,
                                                 @RequestBody @Valid UserSubscriptionDtoIn dto) {
        userSubscriptionService.updateUserSubscription(subscriptionId, userId, planId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("User subscription updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUserSubscription(@PathVariable Integer id) {
        userSubscriptionService.deleteUserSubscription(id);
        return ResponseEntity.status(200).body(new ApiResponse("User subscription deleted successfully"));
    }

    //
    @GetMapping("/user-subscriptions/{user-id}")
    public ResponseEntity getUserSubscriptionsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(userSubscriptionService.getAllUserSubscriptionsByUserId(userId));
    }
    @GetMapping("/user-subscriptions/{status}")
    public ResponseEntity getUserSubscriptionsByStatus(@PathVariable String status) {
        return ResponseEntity.status(200).body(userSubscriptionService.getAllUserSubscriptionsByStatus(status));
    }
}
