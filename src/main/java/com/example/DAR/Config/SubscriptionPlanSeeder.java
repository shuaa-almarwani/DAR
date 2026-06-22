package com.example.DAR.Config;

import com.example.DAR.Model.SubscriptionPlan;
import com.example.DAR.Repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionPlanSeeder implements CommandLineRunner {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public void run(String... args) {
        savePlan("Standard", "Good choice for organized home management", 50.0, false, 3, 50, 20, 50, 20, true, false,
                "https://shuaa.lemonsqueezy.com/checkout/buy/bf5e726e-a0e8-4d83-b9c1-ca11b53946e8",
                "1780423");
        savePlan("Professional", "Best choice for villas and smart homes", 200.0, true, 10, 200, 100, 200, 100, true, true,
                "https://shuaa.lemonsqueezy.com/checkout/buy/77311612-db16-429f-bbcc-3df50d472ad0",
                "1820211");
    }

    private void savePlan(String name, String subtitle, Double price, Boolean isPopular, Integer maxHomes,
                          Integer maxItems, Integer maxSensors, Integer maxNotificationsPerMonth,
                          Integer maxAiReportsPerMonth, Boolean weatherReminderEnabled,
                          Boolean dailyAIReminder ,String lemonSqueezyCheckoutUrl, String lemonSqueezyVariantId) {
        SubscriptionPlan plan = subscriptionPlanRepository.findSubscriptionPlanByName(name);
        if (plan == null) {
            plan = new SubscriptionPlan();
        }
        plan.setName(name);
        plan.setSubtitle(subtitle);
        plan.setPrice(price);
        plan.setIsPopular(isPopular);
        plan.setMaxHomes(maxHomes);
        plan.setMaxItems(maxItems);
        plan.setMaxSensors(maxSensors);
        plan.setMaxNotificationsPerMonth(maxNotificationsPerMonth);
        plan.setMaxAiReportsPerMonth(maxAiReportsPerMonth);
        plan.setWeatherReminderEnabled(weatherReminderEnabled);
        plan.setDailyAIReminder(dailyAIReminder);

        plan.setLemonSqueezyCheckoutUrl(lemonSqueezyCheckoutUrl);
        plan.setLemonSqueezyVariantId(lemonSqueezyVariantId);

        subscriptionPlanRepository.save(plan);
    }
}
