package com.example.DAR.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HomeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private LocalDate installDate;
    @Column(nullable = false)
    private Integer lifespanMonth;
    @Column(nullable = false)
    private LocalTime nextServiceDate;
    @Column(nullable = false)
    private String notes;


    @ManyToOne
    @JoinColumn(name = "homeId", nullable = false)
    private Home home;



}
