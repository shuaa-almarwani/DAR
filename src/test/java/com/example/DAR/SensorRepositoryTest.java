package com.example.DAR;

import com.example.DAR.Model.Home;
import com.example.DAR.Model.Sensor;
import com.example.DAR.Model.User;
import com.example.DAR.Repository.SensorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SensorRepositoryTest {
    @Autowired
    private SensorRepository sensorRepository;
    private Sensor sensor;
    private User user;
    private Home home;

    @BeforeEach
    void setUp() {
        user=new User();
        user.setId(1);
        user.setName("Shahad");
        user.setEmail("shahad@dar.com");
        user.setUsername("shahad123");
        user.setPassword("password123");
        user.setPhoneNumber("0501234567");

        home = new Home();
        home.setId(1);
        home.setUser(user);
        home.setName("Villa Dar");
        home.setAddress("123 King Fahd Road");
        home.setCity("Riyadh");
        home.setBuildYear(2020);

        sensor=new Sensor();
        sensor.setId(1);
        sensor.setLocation("Living Room");
        sensor.setSerialNumber("A1B2-C3D4-E5F6");
        sensor.setHome(home);
    }
    @Test
    @DisplayName("Should find sensor by ID")
    void shouldFindSensorById() {
        Sensor found = sensorRepository.findSensorById(sensor.getId());

        assertNotNull(found);
        assertEquals("Living Room", found.getLocation());
    }

    @Test
    @DisplayName("Should find active sensors by home ID")
    void shouldFindActiveSensorsByHomeId() {
        List<Sensor> activeSensors = sensorRepository.findByHomeIdAndIsActiveTrue(home.getId());

        assertNotNull(activeSensors);
        assertTrue(activeSensors.stream().allMatch(s -> Boolean.TRUE.equals(s.getIsActive())));
    }






}
