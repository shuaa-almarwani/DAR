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

    @Column(nullable = false)
    private String notificationMethod;

    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home;


    @OneToOne
    @JoinColumn(name = "home_item_id")
    private HomeItem homeItem;

    @ManyToOne
    @JoinColumn(name = "maintenance_id", nullable = false)
    private Maintenance maintenance;

}
