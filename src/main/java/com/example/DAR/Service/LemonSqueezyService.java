package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.Model.UserSubscription;
import com.example.DAR.Repository.UserSubscriptionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class LemonSqueezyService {

    private final WebClient webClient;
    private final PaymentService paymentService;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${lemon.squeezy.store.id}")
    private String storeId;

    @Value("${lemon.squeezy.webhook.secret}")
    private String webhookSecret;

    public String createCheckout(Integer userSubscriptionId) {
        UserSubscription userSubscription = userSubscriptionRepository.findUserSubscriptionById(userSubscriptionId);
        if (userSubscription == null) throw new ApiException("User subscription not found");
        String checkoutUrl = userSubscription.getSubscriptionPlan().getLemonSqueezyCheckoutUrl();
        if (checkoutUrl == null) throw new ApiException("No checkout URL configured for this plan");
        return checkoutUrl + "?checkout[custom][userSubscriptionId]=" + userSubscriptionId;
    }

    public void processWebhook(HttpHeaders headers, String rawBody) {
        try {
            String signature = headers.getFirst("X-Signature");
            if (signature == null || !verifySignature(rawBody, signature)) {
                throw new ApiException("Invalid webhook signature");
            }

            JsonNode root = objectMapper.readTree(rawBody);
            String eventName = root.path("meta").path("event_name").asText();

            if ("order_created".equals(eventName)) {
                handleOrderCreated(root);
            }

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Failed to process webhook: " + e.getMessage());
        }
    }

    private void handleOrderCreated(JsonNode root) {
        JsonNode customData = root.path("meta").path("custom_data");
        String subscriptionIdStr = customData.path("userSubscriptionId").asText(null);
        String transactionRef = root.path("data").path("attributes").path("order_number").asText();

        if (subscriptionIdStr == null) return;

        paymentService.activateSubscriptionFromWebhook(Integer.parseInt(subscriptionIdStr), transactionRef);
    }

    private boolean verifySignature(String body, String signature) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] hash = mac.doFinal(body.getBytes(StandardCharsets.UTF_8));
        String computed = HexFormat.of().formatHex(hash);
        return MessageDigest.isEqual(computed.getBytes(StandardCharsets.UTF_8), signature.getBytes(StandardCharsets.UTF_8));
    }
}
