package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.SubscriptionPlanDtoIn;
import com.example.DAR.Service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/subscription-plan")
@RestController
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllPlans() {
        return ResponseEntity.status(200).body(subscriptionPlanService.getAllPlans());
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addPlan(@RequestBody @Valid SubscriptionPlanDtoIn dto) {
        subscriptionPlanService.addPlan(dto);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription plan added successfully"));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePlan(@PathVariable Integer id,
                                     @RequestBody @Valid SubscriptionPlanDtoIn dto) {
        subscriptionPlanService.updatePlan(id, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription plan updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePlan(@PathVariable Integer id) {
        subscriptionPlanService.deletePlan(id);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription plan deleted successfully"));
    }
}
