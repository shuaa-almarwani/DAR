package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.SubscriptionPlanDtoIn;
import com.example.DAR.DTO.Out.SubscriptionPlanDtoOut;
import com.example.DAR.Model.SubscriptionPlan;
import com.example.DAR.Repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final ModelMapper modelMapper;

    public List<SubscriptionPlanDtoOut> getAllPlans() {
        List<SubscriptionPlan> plans = subscriptionPlanRepository.findAll();
        List<SubscriptionPlanDtoOut> dtoOuts = new ArrayList<>();

        for (SubscriptionPlan plan : plans) {
            dtoOuts.add(modelMapper.map(plan, SubscriptionPlanDtoOut.class));
        }
        return dtoOuts;
    }

    public void addPlan(SubscriptionPlanDtoIn dto) {
        SubscriptionPlan plan = modelMapper.map(dto, SubscriptionPlan.class);
        subscriptionPlanRepository.save(plan);
    }

    public void updatePlan(Integer id, SubscriptionPlanDtoIn dto) {
        SubscriptionPlan plan = subscriptionPlanRepository.findSubscriptionPlanById(id);

        if (plan == null) {
            throw new ApiException("Subscription plan not found");
        }

        plan.setName(dto.getName());
        plan.setSubtitle(dto.getSubtitle());
        plan.setPrice(dto.getPrice());
        plan.setIsPopular(dto.getIsPopular());
        plan.setMaxHomes(dto.getMaxHomes());
        plan.setMaxItems(dto.getMaxItems());
        plan.setMaxSensors(dto.getMaxSensors());
        plan.setMaxNotificationsPerMonth(dto.getMaxNotificationsPerMonth());
        plan.setMaxAiReportsPerMonth(dto.getMaxAiReportsPerMonth());
        plan.setWeatherReminderEnabled(dto.getWeatherReminderEnabled());
        plan.setDailyAIReminder(dto.getDailyAIReminder());

        plan.setLemonSqueezyCheckoutUrl(dto.getLemonSqueezyCheckoutUrl());
        plan.setLemonSqueezyVariantId(dto.getLemonSqueezyVariantId());

        subscriptionPlanRepository.save(plan);
    }

    public void deletePlan(Integer id) {
        SubscriptionPlan plan = subscriptionPlanRepository.findSubscriptionPlanById(id);

        if (plan == null) {
            throw new ApiException("Subscription plan not found");
        }

        subscriptionPlanRepository.delete(plan);
    }


}
