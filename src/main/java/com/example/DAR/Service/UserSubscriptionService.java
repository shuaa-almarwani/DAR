package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.Enums.PaymentStatus;
import com.example.DAR.Enums.UserSubscriptionStatus;
import com.example.DAR.DTO.Out.UserSubscriptionDtoOut;
import com.example.DAR.Model.SubscriptionPlan;
import com.example.DAR.Model.User;
import com.example.DAR.Model.UserSubscription;
import com.example.DAR.Repository.SubscriptionPlanRepository;
import com.example.DAR.Repository.UserRepository;
import com.example.DAR.Repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSubscriptionService {
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;

    public List<UserSubscriptionDtoOut> getAllUserSubscriptions() {
        List<UserSubscription> subscriptions = userSubscriptionRepository.findAll();
        List<UserSubscriptionDtoOut> dtoOuts = new ArrayList<>();

        for (UserSubscription subscription : subscriptions) {
            dtoOuts.add(mapToDto(subscription));
        }

        return dtoOuts;
    }

    public UserSubscriptionDtoOut createUserSubscription(Integer userId, Integer planId) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }

        SubscriptionPlan plan = subscriptionPlanRepository.findSubscriptionPlanById(planId);

        if (plan == null) {
            throw new ApiException("Subscription plan not found");
        }

        UserSubscription userSubscription = new UserSubscription();

        userSubscription.setUser(user);
        userSubscription.setSubscriptionPlan(plan);
        userSubscription.setStartDate(LocalDate.now());
        userSubscription.setEndDate(LocalDate.now().plusDays(29));

        if (plan.getPrice() == 0) {
            userSubscription.setStatus(UserSubscriptionStatus.ACTIVE);
            userSubscription.setPaymentStatus(PaymentStatus.PAID);
        } else {
            userSubscription.setStatus(UserSubscriptionStatus.PENDING);
            userSubscription.setPaymentStatus(PaymentStatus.UNPAID);
        }

        UserSubscription savedSubscription = userSubscriptionRepository.save(userSubscription);
        if (plan.getPrice() == 0) {
            notificationService.sendSubscriptionActivatedNotification(user, plan.getName());
        } else {
            notificationService.sendSubscriptionPendingPaymentNotification(user, plan.getName());
        }
        return mapToDto(savedSubscription);
    }

    public void deleteUserSubscription(Integer id) {

        UserSubscription subscription = userSubscriptionRepository.findUserSubscriptionById(id);
        if (subscription == null) {
            throw new ApiException("User subscription not found");
        }


        userSubscriptionRepository.delete(subscription);
    }



    public UserSubscriptionDtoOut upgradeUserSubscription(Integer userId, Integer planId) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }

        SubscriptionPlan plan = subscriptionPlanRepository.findSubscriptionPlanById(planId);

        if (plan == null) {
            throw new ApiException("Subscription plan not found");
        }

        UserSubscription activeSubscription = userSubscriptionRepository.findUserSubscriptionByUserIdAndStatus(userId, UserSubscriptionStatus.ACTIVE);

        if (activeSubscription == null) {
            throw new ApiException("Active subscription not found");
        }

        if (activeSubscription.getSubscriptionPlan().getId().equals(planId)) {
            throw new ApiException("User is already subscribed to this plan");
        }

        UserSubscription newSubscription = new UserSubscription();
        newSubscription.setUser(user);
        newSubscription.setSubscriptionPlan(plan);
        newSubscription.setStartDate(LocalDate.now());
        newSubscription.setEndDate(LocalDate.now().plusDays(29));

        if (plan.getPrice() == 0) {
            activeSubscription.setStatus(UserSubscriptionStatus.CANCELLED);
            userSubscriptionRepository.save(activeSubscription);
            newSubscription.setStatus(UserSubscriptionStatus.ACTIVE);
            newSubscription.setPaymentStatus(PaymentStatus.PAID);
        } else {
            newSubscription.setStatus(UserSubscriptionStatus.PENDING);
            newSubscription.setPaymentStatus(PaymentStatus.UNPAID);
        }

        UserSubscription savedSubscription = userSubscriptionRepository.save(newSubscription);

        if (plan.getPrice() == 0) {
            notificationService.sendSubscriptionActivatedNotification(user, plan.getName());
        } else {
            notificationService.sendSubscriptionPendingPaymentNotification(user, plan.getName());
        }

        return mapToDto(savedSubscription);
    }

    public List<UserSubscriptionDtoOut> getAllUserSubscriptionsByUserId(Integer userId) {
        List<UserSubscription> subscriptions = userSubscriptionRepository.findUserSubscriptionsByUserId(userId);
        List<UserSubscriptionDtoOut> dtoOuts = new ArrayList<>();
        for (UserSubscription subscription : subscriptions) {
            dtoOuts.add(mapToDto(subscription));
        }
        return dtoOuts;
    }

    public List<UserSubscriptionDtoOut> getAllUserSubscriptionsByStatus(UserSubscriptionStatus status) {
        List<UserSubscription> subscriptions = userSubscriptionRepository.findUserSubscriptionsByStatus(status);
        List<UserSubscriptionDtoOut> dtoOuts = new ArrayList<>();
        for (UserSubscription subscription : subscriptions) {
            dtoOuts.add(mapToDto(subscription));
        }
        return dtoOuts;
    }

    private UserSubscriptionDtoOut mapToDto(UserSubscription subscription) {
        UserSubscriptionDtoOut dto = modelMapper.map(subscription, UserSubscriptionDtoOut.class);
        dto.setUserId(subscription.getUser().getId());
        dto.setUsername(subscription.getUser().getUsername());
        dto.setPlanName(subscription.getSubscriptionPlan().getName());
        return dto;
    }
}
