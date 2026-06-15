package com.example.DAR.Model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false)
    private String paymentMethod;
    @Column(nullable = false)
    private LocalDate paymentDate;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private String transactionReference;

        @ManyToOne
    @JoinColumn(name = "userSubscriptionId", nullable = false)
    private UserSubscription userSubscription;

}
