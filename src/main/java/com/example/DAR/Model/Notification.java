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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private LocalDate  sentAt;

        @ManyToOne
    @JoinColumn(name = "homeId", nullable = false)
    private Home home;
}
