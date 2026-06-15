package com.example.DAR.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false)
    private Double readingValue;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private LocalDateTime readingDate;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensors;

}
