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
public class MaintenanceReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDate reminderDate;

    @Column(nullable = false)
    private String season;

    @Column(nullable = false)
    private String weatherCondition;

    @Column(nullable = false)
    private Boolean isSent;

    @Column(nullable = false)
    private LocalDate createdAt;


    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home;


    @OneToOne
    @JoinColumn(name = "homeItem_id")
    private HomeItem homeItem;



}
