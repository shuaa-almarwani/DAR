package com.example.DAR.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime sentAt;

    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
