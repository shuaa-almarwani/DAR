package com.example.DAR;

import com.example.DAR.Enums.PaymentStatus;
import com.example.DAR.Enums.UserSubscriptionStatus;
import com.example.DAR.Model.Payment;
import com.example.DAR.Model.SubscriptionPlan;
import com.example.DAR.Model.User;
import com.example.DAR.Model.UserSubscription;
import com.example.DAR.Repository.PaymentRepository;
import com.example.DAR.Repository.SubscriptionPlanRepository;
import com.example.DAR.Repository.UserRepository;
import com.example.DAR.Repository.UserSubscriptionRepository;
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
public class UserSubscriptionAndPaymentRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    PaymentRepository paymentRepository;

    User user;
    User otherUser;
    SubscriptionPlan plan;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        userSubscriptionRepository.deleteAll();
        subscriptionPlanRepository.deleteAll();
        userRepository.deleteAll();

        user = userRepository.save(createUser("user1"));
        otherUser = userRepository.save(createUser("user2"));
        plan = subscriptionPlanRepository.save(createPlan());
    }

    // Test #1: Find expired active subscriptions
    @Test
    public void findExpiredSubscriptionsTest() {
        UserSubscription expiredActive = userSubscriptionRepository.save(createSubscription(user, UserSubscriptionStatus.ACTIVE, LocalDate.now().minusDays(1)));
        userSubscriptionRepository.save(createSubscription(user, UserSubscriptionStatus.ACTIVE, LocalDate.now().plusDays(1)));
        userSubscriptionRepository.save(createSubscription(user, UserSubscriptionStatus.CANCELLED, LocalDate.now().minusDays(1)));

        List<UserSubscription> result = userSubscriptionRepository.findExpired(UserSubscriptionStatus.ACTIVE, LocalDate.now());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(expiredActive.getId(), result.get(0).getId());
    }

    // Test #2: Find user subscriptions by pending and active statuses
    @Test
    public void findByUserAndStatusesTest() {
        UserSubscription pending = userSubscriptionRepository.save(createSubscription(user, UserSubscriptionStatus.PENDING, null));
        UserSubscription active = userSubscriptionRepository.save(createSubscription(user, UserSubscriptionStatus.ACTIVE, LocalDate.now().plusDays(10)));
        userSubscriptionRepository.save(createSubscription(user, UserSubscriptionStatus.EXPIRED, LocalDate.now().minusDays(1)));
        userSubscriptionRepository.save(createSubscription(otherUser, UserSubscriptionStatus.ACTIVE, LocalDate.now().plusDays(10)));

        List<UserSubscription> result = userSubscriptionRepository.findByUserAndStatuses(
                user.getId(),
                List.of(UserSubscriptionStatus.PENDING, UserSubscriptionStatus.ACTIVE)
        );

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().anyMatch(subscription -> subscription.getId().equals(pending.getId())));
        Assertions.assertTrue(result.stream().anyMatch(subscription -> subscription.getId().equals(active.getId())));
    }

    // Test #3: Find payments by user id
    @Test
    public void findPaymentsByUserTest() {
        UserSubscription userSubscription = userSubscriptionRepository.save(createSubscription(user, UserSubscriptionStatus.ACTIVE, LocalDate.now().plusDays(10)));
        UserSubscription otherSubscription = userSubscriptionRepository.save(createSubscription(otherUser, UserSubscriptionStatus.ACTIVE, LocalDate.now().plusDays(10)));
        Payment userPayment = paymentRepository.save(createPayment(userSubscription, "TX-1"));
        paymentRepository.save(createPayment(otherSubscription, "TX-2"));

        List<Payment> result = paymentRepository.findByUser(user.getId());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(userPayment.getId(), result.get(0).getId());
    }


    private User createUser(String username) {
        User user = new User();
        user.setName(username);
        user.setEmail(username + "@dar.com");
        user.setUsername(username);
        user.setPassword("password");
        user.setPhoneNumber("0500000000");
        user.setRole("USER");
        user.setCreateAt(LocalDate.now());
        user.setAiCounter(0);
        return user;
    }

    private SubscriptionPlan createPlan() {
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setName("Standard");
        plan.setSubtitle("Standard plan");
        plan.setPrice(50.0);
        plan.setIsPopular(false);
        plan.setMaxHomes(3);
        plan.setMaxItems(50);
        plan.setMaxSensors(20);
        plan.setMaxNotificationsPerMonth(50);
        plan.setMaxAiReportsPerMonth(20);
        plan.setWeatherReminderEnabled(true);
        plan.setDailyAIReminder(false);
        return plan;
    }

    private UserSubscription createSubscription(User user, UserSubscriptionStatus status, LocalDate endDate) {
        UserSubscription subscription = new UserSubscription();
        subscription.setUser(user);
        subscription.setSubscriptionPlan(plan);
        subscription.setStartDate(endDate == null ? null : LocalDate.now().minusDays(10));
        subscription.setEndDate(endDate);
        subscription.setStatus(status);
        subscription.setPaymentStatus(status == UserSubscriptionStatus.PENDING ? PaymentStatus.UNPAID : PaymentStatus.PAID);
        return subscription;
    }

    private Payment createPayment(UserSubscription subscription, String reference) {
        Payment payment = new Payment();
        payment.setUserSubscription(subscription);
        payment.setAmount(subscription.getSubscriptionPlan().getPrice());
        payment.setPaymentMethod("CARD");
        payment.setPaymentDate(LocalDate.now());
        payment.setStatus(PaymentStatus.PAID);
        payment.setTransactionReference(reference);
        return payment;
    }
}
