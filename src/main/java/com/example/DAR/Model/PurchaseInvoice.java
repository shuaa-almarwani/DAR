package com.example.DAR.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PurchaseInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String store;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String warrantyNote;

    @Column(nullable = false)
    private LocalDate warrantyExpiry;


    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home;

}
