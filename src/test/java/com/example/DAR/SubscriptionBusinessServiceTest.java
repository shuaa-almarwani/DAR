package com.example.DAR;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.HomeDTOIn;
import com.example.DAR.DTO.Out.UserSubscriptionDtoOut;
import com.example.DAR.Enums.HomePropertyType;
import com.example.DAR.Enums.PaymentStatus;
import com.example.DAR.Enums.UserSubscriptionStatus;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.SubscriptionPlan;
import com.example.DAR.Model.User;
import com.example.DAR.Model.UserSubscription;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.SubscriptionPlanRepository;
import com.example.DAR.Repository.UserRepository;
import com.example.DAR.Repository.UserSubscriptionRepository;
import com.example.DAR.Service.HomeService;
import com.example.DAR.Service.NotificationService;
import com.example.DAR.Service.UserSubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionBusinessServiceTest {

    @InjectMocks
    HomeService homeService;

    @InjectMocks
    UserSubscriptionService userSubscriptionService;

    @Mock
    HomeRepository homeRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserSubscriptionRepository userSubscriptionRepository;

    @Mock
    SubscriptionPlanRepository subscriptionPlanRepository;

    @Mock
    NotificationService notificationService;

    @Mock
    ModelMapper modelMapper;

    User user;
    SubscriptionPlan plan;
    HomeDTOIn homeDTOIn;
    UserSubscriptionDtoOut userSubscriptionDtoOut;

    @BeforeEach
    void setUp() {
        user = createUser();
        plan = createPlan();
        homeDTOIn = createHomeDto();

        userSubscriptionDtoOut = new UserSubscriptionDtoOut();
        userSubscriptionDtoOut.setId(10);
        userSubscriptionDtoOut.setUserId(user.getId());
        userSubscriptionDtoOut.setUsername(user.getUsername());
        userSubscriptionDtoOut.setPlanName(plan.getName());
        userSubscriptionDtoOut.setStatus(UserSubscriptionStatus.PENDING);
        userSubscriptionDtoOut.setPaymentStatus(PaymentStatus.UNPAID);
    }

    // Test #1: Free user cannot add more than one home
    @Test
    @DisplayName("Should throw exception when free user adds more than one home")
    public void addHomeFreeUserCannotAddMoreThanOneHomeTest() {

        when(userRepository.findUserById(1)).thenReturn(user);
        when(userSubscriptionRepository.findUserSubscriptionByUserIdAndStatus(1, UserSubscriptionStatus.ACTIVE)).thenReturn(null);
        when(homeRepository.findHomesByUserId(1)).thenReturn(List.of(new Home()));

        ApiException exception = assertThrows(ApiException.class, () -> homeService.addHome(1, homeDTOIn));

        assertEquals("You have reached the maximum number of homes for your plan", exception.getMessage());
        verify(homeRepository, never()).save(any(Home.class));
    }

    // Test #2: Create subscription as pending and unpaid
    @Test
    @DisplayName("Should create pending unpaid subscription")
    public void createUserSubscriptionCreatesPendingUnpaidSubscriptionTest() {

        when(userRepository.findUserById(1)).thenReturn(user);
        when(userSubscriptionRepository.findByUserAndStatuses(eq(1), anyList())).thenReturn(List.of());
        when(subscriptionPlanRepository.findSubscriptionPlanById(2)).thenReturn(plan);
        when(userSubscriptionRepository.save(any(UserSubscription.class))).thenAnswer(invocation -> {
            UserSubscription subscription = invocation.getArgument(0);
            subscription.setId(10);
            return subscription;
        });
        when(modelMapper.map(any(UserSubscription.class), eq(UserSubscriptionDtoOut.class))).thenReturn(userSubscriptionDtoOut);

        UserSubscriptionDtoOut result = userSubscriptionService.createUserSubscription(1, 2);

        ArgumentCaptor<UserSubscription> captor = ArgumentCaptor.forClass(UserSubscription.class);
        verify(userSubscriptionRepository).save(captor.capture());
        assertEquals(UserSubscriptionStatus.PENDING, captor.getValue().getStatus());
        assertEquals(PaymentStatus.UNPAID, captor.getValue().getPaymentStatus());
        assertNull(captor.getValue().getStartDate());
        assertNull(captor.getValue().getEndDate());
        assertEquals("Standard", result.getPlanName());
        verify(notificationService).sendSubscriptionPendingPaymentNotification(user, "Standard");
    }



    private User createUser() {
        User user = new User();
        user.setId(1);
        user.setName("Samya");
        user.setEmail("samya@dar.com");
        user.setUsername("samya");
        user.setPassword("password");
        user.setPhoneNumber("0500000000");
        user.setRole("USER");
        user.setCreateAt(LocalDate.now());
        user.setAiCounter(0);
        return user;
    }

    private SubscriptionPlan createPlan() {
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setId(2);
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

    private HomeDTOIn createHomeDto() {
        HomeDTOIn dto = new HomeDTOIn();
        dto.setName("Home");
        dto.setAddress("Riyadh");
        dto.setCity("Riyadh");
        dto.setLatitude(24.7136);
        dto.setLongitude(46.6753);
        dto.setBuildYear(2020);
        dto.setPropertyType(HomePropertyType.APARTMENT);
        return dto;
    }
}
