package com.example.DAR.Model;

import com.example.DAR.Enums.HomeItemCategory;
import com.example.DAR.Enums.HomeItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HomeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HomeItemCategory category;

    @Column
    private String customCategory;

    @Column(nullable = false)
    private String brand;

    @Column
    private String model;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDate installDate;

    @Column
    private LocalDate purchaseDate;

    @Column(nullable = false)
    private Integer lifespanMonth;

    @Column
    private Integer warrantyMonths;

    @Column(nullable = false)
    private LocalDate nextServiceDate;

    @Column
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HomeItemStatus status;

    @Column
    private String notes;


    @ManyToOne
    @JoinColumn(name = "home_id", nullable = false)
    private Home home;



}
