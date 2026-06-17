package com.example.DAR.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Home {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Integer buildYear;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<MaintenanceReminder> maintenanceReminders;


    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Maintenance> maintenances;


    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Bill> bills;


    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Sensor> sensors;


    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<PurchaseInvoice> purchaseInvoices;

}
