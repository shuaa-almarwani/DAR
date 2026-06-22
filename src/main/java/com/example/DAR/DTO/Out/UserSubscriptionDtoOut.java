package com.example.DAR.DTO.Out;

import com.example.DAR.Enums.PaymentStatus;
import com.example.DAR.Enums.UserSubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscriptionDtoOut {
    private Integer id;
    private Integer userId;
    private String username;
    private String planName;
    private LocalDate startDate;
    private LocalDate endDate;
    private UserSubscriptionStatus status;
    private PaymentStatus paymentStatus;
    private String checkoutUrl;
}
