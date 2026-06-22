package com.example.DAR.DTO.Out;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPlanDtoOut {
    private Integer id;
    private String name;
    private String subtitle;
    private Double price;
    private Boolean isPopular;
    private Integer maxHomes;
    private Integer maxItems;
    private Integer maxSensors;
    private Integer maxNotificationsPerMonth;
    private Integer maxAiReportsPerMonth;
    private Boolean weatherReminderEnabled;
    private Boolean dailyAIReminder;

    private String lemonSqueezyCheckoutUrl;
    private String checkoutUrl;
}
