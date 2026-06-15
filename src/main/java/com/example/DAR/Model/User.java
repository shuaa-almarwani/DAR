package com.example.DAR.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(100)",nullable = false)
    private String name;
    @Column(columnDefinition = "varchar(255)",nullable = false,unique = false)
    private String email;
    @Column(columnDefinition = "varchar(100)",nullable = false,unique = false)
    private String username;
    @Column(columnDefinition = "varchar(255)",nullable = false)
    private String password;
    @Column(columnDefinition = "varchar(255)",nullable = false)
    private String phoneNumber;
    private LocalDate createAt;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<UserSubscription> userSubscriptions;
}
