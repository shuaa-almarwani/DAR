package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.PaymentDtoIn;
import com.example.DAR.Security.SecurityService;
import com.example.DAR.Service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/payment")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final SecurityService securityService;

    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllPayments() {
        return ResponseEntity.status(200).body(paymentService.getAllPayments());
    }

    @PostMapping("/add/{userSubscriptionId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserSubscription(#userSubscriptionId)")
    public ResponseEntity<?> addPayment(@PathVariable Integer userSubscriptionId,
                                     @RequestBody @Valid PaymentDtoIn dto) {
        paymentService.addPayment(userSubscriptionId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Payment added successfully"));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePayment(@PathVariable Integer id) {
        paymentService.deletePayment(id);
        return ResponseEntity.status(200).body(new ApiResponse("Payment deleted successfully"));
    }


    @GetMapping("/user-payments")
    public ResponseEntity<?> getAllPaymentsByUser() {
        return ResponseEntity.status(200).body(paymentService.getAllPaymentsByUser(securityService.getCurrentUserId()));
    }
}
