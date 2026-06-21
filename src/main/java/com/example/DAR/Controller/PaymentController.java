package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.PaymentDtoIn;
import com.example.DAR.Service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/payment")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllPayments() {
        return ResponseEntity.status(200).body(paymentService.getAllPayments());
    }

    @PostMapping("/add/{userSubscriptionId}")
    public ResponseEntity<?> addPayment(@PathVariable Integer userSubscriptionId,
                                     @RequestBody @Valid PaymentDtoIn dto) {
        paymentService.addPayment(userSubscriptionId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Payment added successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Integer id) {
        paymentService.deletePayment(id);
        return ResponseEntity.status(200).body(new ApiResponse("Payment deleted successfully"));
    }


    @GetMapping("/get/user/{userId}")
    public ResponseEntity<?> getAllPaymentsByUser(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(paymentService.getAllPaymentsByUser(userId));
    }
}
