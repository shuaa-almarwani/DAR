package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
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

    public List<UserSubscriptionDtoOut> getAllUserSubscriptions() {
        List<UserSubscription> subscriptions = userSubscriptionRepository.findAll();
        List<UserSubscriptionDtoOut> dtoOuts = new ArrayList<>();

        for (UserSubscription subscription : subscriptions) {
            dtoOuts.add(convertToDtoOut(subscription));
        }

        return dtoOuts;
    }
    // mapping because UserSubscription has relations with User and SubscriptionPlan.
    private UserSubscriptionDtoOut convertToDtoOut(UserSubscription subscription) {

        UserSubscriptionDtoOut dto = new UserSubscriptionDtoOut();

        dto.setId(subscription.getId());

        dto.setUserId(subscription.getUser().getId());
        dto.setUsername(subscription.getUser().getUsername());

        dto.setPlanId(subscription.getSubscriptionPlan().getId());
        dto.setPlanName(subscription.getSubscriptionPlan().getName());

        dto.setStartDate(subscription.getStartDate());
        dto.setEndDate(subscription.getEndDate());
        dto.setStatus(subscription.getStatus());
        dto.setPaymentStatus(subscription.getPaymentStatus());

        return dto;
    }
    public void addUserSubscription(Integer userId, Integer planId, UserSubscriptionDtoOut dto) {

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

        userSubscription.setStatus("ACTIVE");
        userSubscription.setPaymentStatus("UNPAID");

        userSubscriptionRepository.save(userSubscription);
    }

    public void updateUserSubscription(Integer subscriptionId,
                                       Integer userId,
                                       Integer planId,
                                       UserSubscriptionDtoOut dto) {

        UserSubscription oldSubscription =
                userSubscriptionRepository.findUserSubscriptionById(subscriptionId);

        if (oldSubscription == null) {
            throw new ApiException("User subscription not found");
        }

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }

        SubscriptionPlan plan = subscriptionPlanRepository.findSubscriptionPlanById(planId);

        if (plan == null) {
            throw new ApiException("Subscription plan not found");
        }

        oldSubscription.setUser(user);
        oldSubscription.setSubscriptionPlan(plan);
        oldSubscription.setStartDate(LocalDate.now());
        oldSubscription.setEndDate(LocalDate.now().plusDays(29));

        userSubscriptionRepository.save(oldSubscription);
    }

    public void deleteUserSubscription(Integer id) {

        UserSubscription subscription = userSubscriptionRepository.findUserSubscriptionById(id);
        if (subscription == null) {
            throw new ApiException("User subscription not found");
        }


        userSubscriptionRepository.delete(subscription);
    }

    public List<UserSubscriptionDtoOut> getAllUserSubscriptionsByUserId(Integer userId) {
        List<UserSubscription> subscriptions = userSubscriptionRepository.findUserSubscriptionsByUserId(userId);
        List<UserSubscriptionDtoOut> dtoOuts = new ArrayList<>();
        for (UserSubscription subscription : subscriptions) {
            dtoOuts.add(convertToDtoOut(subscription));
        }
        return dtoOuts;
    }

    public List<UserSubscriptionDtoOut> getAllUserSubscriptionsByStatus(String status) {
        List<UserSubscription> subscriptions = userSubscriptionRepository.findUserSubscriptionsByStatus(status);
        List<UserSubscriptionDtoOut> dtoOuts = new ArrayList<>();
        for (UserSubscription subscription : subscriptions) {
            dtoOuts.add(convertToDtoOut(subscription));
        }
        return dtoOuts;
    }
}