package com.example.DAR;

import com.example.DAR.Enums.HomePropertyType;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.Notification;
import com.example.DAR.Model.User;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.NotificationRepository;
import com.example.DAR.Repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NotificationRepositoryTest {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HomeRepository homeRepository;

    User user;
    Home home;
    Notification notification1, notification2;

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

        notification1 = new Notification();
        notification1.setTitle("نصيحة ذكية مجانية من دار");
        notification1.setMessage("Free smart tip message");
        notification1.setType("FREE_SMART_TIP_UPSELL");
        notification1.setSentAt(LocalDateTime.now());
        notification1.setIsRead(false);
        notification1.setUser(user);
        notification1.setHome(null);

        notification2 = new Notification();
        notification2.setTitle("تنبيه ذكي حسب الطقس");
        notification2.setMessage("Weather alert message");
        notification2.setType("WEATHER_HUMIDITY_CHECK");
        notification2.setSentAt(LocalDateTime.now());
        notification2.setIsRead(true);
        notification2.setUser(user);
        notification2.setHome(home);
    }

    // Test #3: Find notifications by user id
    @Test
    public void findNotificationsByUserIdTest() {

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);

        List<Notification> notifications =
                notificationRepository.findNotificationsByUserId(user.getId());

        Assertions.assertEquals(2, notifications.size());
        Assertions.assertEquals(user.getId(), notifications.get(0).getUser().getId());
    }

    // Test #4: Check if free smart tip notification already exists
    @Test
    public void existsNotificationByUserIdAndTypeTest() {

        notificationRepository.save(notification1);

        boolean exists =
                notificationRepository.existsNotificationByUserIdAndType(
                        user.getId(),
                        "FREE_SMART_TIP_UPSELL"
                );

        Assertions.assertTrue(exists);
    }
}
