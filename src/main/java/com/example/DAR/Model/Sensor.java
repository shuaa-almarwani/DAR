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
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String location;
// اتوقع نقدر نخليه ديفولت
    @Column(nullable = false)
    private Boolean isActive;
    // اتوقع نقدر نخليه ديفولت
    @Column(nullable = false)
    private LocalDate lastPing;
    @Column(nullable = false)
    private String  serialNumber;

    private String imageUrl;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isRunning = false;
    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home;



}
