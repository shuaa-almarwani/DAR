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
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private LocalDate billMonth;

    @Column(nullable = false)
    private Integer consumption;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private Boolean isInstallment;

    @Column(nullable = false)
    private Integer totalInstallment;

    @Column(nullable = false)
    private Integer paidInstallment;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Boolean isAnomaly;

    @Column(nullable = false)
    private String imageUrl;


    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home;


}
