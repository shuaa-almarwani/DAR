package com.example.DAR.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer maxHomes;

    @Column(nullable = false)
    private Integer maxItems;

    @Column(nullable = false)
    private Integer maxNotificationsPerMonth;

    @Column(nullable = false)
    private Integer maxAiReportsPerMonth;

    @Column(nullable = false)
    private Boolean weatherReminderEnabled;

    @Column(nullable = false)
    private Boolean usageSpikeDetectionEnabled;


    @OneToMany(mappedBy = "subscriptionPlan")
    @JsonIgnore
    private Set<UserSubscription> userSubscriptions;
}
