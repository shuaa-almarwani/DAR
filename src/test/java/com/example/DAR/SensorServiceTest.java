package com.example.DAR;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.SensorDtoIn;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.Sensor;
import com.example.DAR.Model.User;
import com.example.DAR.Model.UserSubscription;
import com.example.DAR.Model.SubscriptionPlan;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.UserSubscriptionRepository;
import com.example.DAR.Service.SensorService;
import com.example.DAR.Enums.UserSubscriptionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SensorServiceTest {


    @Mock private HomeRepository homeRepository;
    @Mock private UserSubscriptionRepository userSubscriptionRepository;


    @InjectMocks
    private SensorService sensorService;

    private User user;
    private Home home;
    private Sensor sensor;
    private SubscriptionPlan plan;
    private UserSubscription subscription;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Shahad");
        user.setRole("USER");

        plan = new SubscriptionPlan();
        plan.setMaxSensors(5);

        subscription = new UserSubscription();
        subscription.setSubscriptionPlan(plan);

        home = new Home();
        home.setId(1);
        home.setUser(user);

        sensor = new Sensor();
        sensor.setId(1);
        sensor.setLocation("Living Room");
        sensor.setIsActive(false);
        sensor.setIsRunning(false);
        sensor.setHome(home);
        sensor.setLastPing(LocalDate.now());
    }

    @Test
    @DisplayName("Should throw exception when home not found on addSensor")
    void addSensor_homeNotFound_throwsException() {
        when(homeRepository.findHomeById(99)).thenReturn(null);

        assertThrows(ApiException.class, () -> sensorService.addSensor(99, new SensorDtoIn()));
    }

    @Test
    @DisplayName("Should throw exception when no active subscription on addSensor")
    void addSensor_noSubscription_throwsException() {
        when(homeRepository.findHomeById(1)).thenReturn(home);
        when(userSubscriptionRepository.findUserSubscriptionByUserIdAndStatus(1, UserSubscriptionStatus.ACTIVE)).thenReturn(null);

        assertThrows(ApiException.class, () -> sensorService.addSensor(1, new SensorDtoIn()));
    }


}
