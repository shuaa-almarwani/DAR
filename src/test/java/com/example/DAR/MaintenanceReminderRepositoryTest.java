package com.example.DAR;

import com.example.DAR.Enums.HomeItemCategory;
import com.example.DAR.Enums.HomeItemStatus;
import com.example.DAR.Enums.HomePropertyType;
import com.example.DAR.Model.*;
import com.example.DAR.Repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MaintenanceReminderRepositoryTest {

    @Autowired
    MaintenanceReminderRepository maintenanceReminderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HomeRepository homeRepository;

    @Autowired
    HomeItemRepository homeItemRepository;

    @Autowired
    MaintenanceRepository maintenanceRepository;

    User user;
    Home home;
    MaintenanceReminder reminder1, reminder2;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setName("Shuaa");
        user.setEmail("shuaa@test.com");
        user.setUsername("shuaa");
        user.setPassword("123456");
        user.setPhoneNumber("0555555555");
        user.setRole("USER");
        user.setCreateAt(LocalDate.now());
        user.setSmartAlertsEnabled(true);
        user.setSmartAlertIntroSent(false);
        userRepository.save(user);

        home = new Home();
        home.setName("My Home");
        home.setAddress("Riyadh");
        home.setCity("Riyadh");
        home.setLatitude(24.7136);
        home.setLongitude(46.6753);
        home.setBuildYear(2020);
        home.setPropertyType(HomePropertyType.APARTMENT);
        home.setUser(user);
        homeRepository.save(home);
        HomeItem homeItem1, homeItem2;
        homeItem1 = new HomeItem();
        homeItem1.setName("AC");
        homeItem1.setCategory(HomeItemCategory.AC);
        homeItem1.setBrand("LG");
        homeItem1.setLocation("Living Room");
        homeItem1.setInstallDate(LocalDate.now().minusYears(1));
        homeItem1.setLifespanMonth(60);
        homeItem1.setNextServiceDate(LocalDate.now().plusMonths(1));
        homeItem1.setStatus(HomeItemStatus.ACTIVE);
        homeItem1.setHome(home);
        homeItemRepository.save(homeItem1);

        homeItem2 = new HomeItem();
        homeItem2.setName("Heater");
        homeItem2.setCategory(HomeItemCategory.AC);
        homeItem2.setBrand("Samsung");
        homeItem2.setLocation("Bedroom");
        homeItem2.setInstallDate(LocalDate.now().minusYears(1));
        homeItem2.setLifespanMonth(60);
        homeItem2.setNextServiceDate(LocalDate.now().plusMonths(1));
        homeItem2.setStatus(HomeItemStatus.ACTIVE);
        homeItem2.setHome(home);
        homeItemRepository.save(homeItem2);

        Maintenance maintenance1, maintenance2;
        maintenance1 = new Maintenance();
        maintenance1.setTitle("AC Maintenance");
        maintenance1.setDescription("Clean AC filter");
        maintenance1.setMaintenanceDate(LocalDate.now().plusDays(5));
        maintenance1.setCost(100.0);
        maintenance1.setStatus("SCHEDULED");
        maintenance1.setPriority("HIGH");
        maintenance1.setNotes("Check filter");
        maintenance1.setHome(home);
        maintenance1.setHomeItem(homeItem1);
        maintenanceRepository.save(maintenance1);

        maintenance2 = new Maintenance();
        maintenance2.setTitle("Heater Maintenance");
        maintenance2.setDescription("Check heater");
        maintenance2.setMaintenanceDate(LocalDate.now().plusDays(7));
        maintenance2.setCost(150.0);
        maintenance2.setStatus("SCHEDULED");
        maintenance2.setPriority("MEDIUM");
        maintenance2.setNotes("Check heater before winter");
        maintenance2.setHome(home);
        maintenance2.setHomeItem(homeItem2);
        maintenanceRepository.save(maintenance2);

        reminder1 = new MaintenanceReminder();
        reminder1.setTitle("AC Reminder");
        reminder1.setMessage("Clean the AC filter");
        reminder1.setReminderDate(LocalDate.now());
        reminder1.setSeason("SUMMER");
        reminder1.setWeatherCondition("HOT");
        reminder1.setIsSent(false);
        reminder1.setCreatedAt(LocalDate.now());
        reminder1.setNotificationMethod("EMAIL");
        reminder1.setHome(home);
        reminder1.setHomeItem(homeItem1);
        reminder1.setMaintenance(maintenance1);

        reminder2 = new MaintenanceReminder();
        reminder2.setTitle("Winter Reminder");
        reminder2.setMessage("Check heater");
        reminder2.setReminderDate(LocalDate.now().plusDays(3));
        reminder2.setSeason("WINTER");
        reminder2.setWeatherCondition("COLD");
        reminder2.setIsSent(true);
        reminder2.setCreatedAt(LocalDate.now());
        reminder2.setNotificationMethod("WHATSAPP");
        reminder2.setHome(home);
        reminder2.setHomeItem(homeItem2);
        reminder2.setMaintenance(maintenance2);
    }

    // Test #1: Find maintenance reminders by isSent status
    @Test
    public void findMaintenanceRemindersByIsSentTest() {

        maintenanceReminderRepository.save(reminder1);
        maintenanceReminderRepository.save(reminder2);

        List<MaintenanceReminder> reminders =
                maintenanceReminderRepository.findMaintenanceRemindersByIsSent(false);

        Assertions.assertEquals(1, reminders.size());
        Assertions.assertEquals(false, reminders.get(0).getIsSent());
    }

    // Test #2: Find maintenance reminders by season
    @Test
    public void findMaintenanceRemindersBySeasonTest() {

        maintenanceReminderRepository.save(reminder1);
        maintenanceReminderRepository.save(reminder2);

        List<MaintenanceReminder> reminders =
                maintenanceReminderRepository.findMaintenanceRemindersBySeason("SUMMER");

        Assertions.assertEquals(1, reminders.size());
        Assertions.assertEquals("SUMMER", reminders.get(0).getSeason());
    }
}
