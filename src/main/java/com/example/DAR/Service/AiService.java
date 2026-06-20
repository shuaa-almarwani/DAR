package com.example.DAR.Service;


import com.example.DAR.Api.ApiException;
import lombok.RequiredArgsConstructor;

import com.example.DAR.Model.HomeItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    @Value("${anthropic.api.key}")
    private String anthropicApiKey;


    public String callClaudeApiText(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", anthropicApiKey);
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("model", "claude-sonnet-4-5");
        requestBody.put("max_tokens", 300);
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.anthropic.com/v1/messages", request, Map.class);
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("content")) {
                List<Map<String, Object>> content = (List<Map<String, Object>>) body.get("content");
                if (!content.isEmpty()) return (String) content.get(0).get("text");
            }
            throw new ApiException("Failed to get response from AI");
        } catch (Exception e) {
            throw new ApiException("AI evaluation failed: " + e.getMessage());
        }
    }

    public  String callClaudeApi(String prompt){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", anthropicApiKey);
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("model", "claude-sonnet-4-5");
        requestBody.put("max_tokens", 1000);
        requestBody.put("system", "Respond ONLY with raw JSON. No markdown, no backticks, no explanation.");
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.anthropic.com/v1/messages",
                    request,
                    Map.class
            );


            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("content")) {
                List<Map<String, Object>> content = (List<Map<String, Object>>) body.get("content");
                if (!content.isEmpty()) {
                    String raw = (String) content.get(0).get("text"); // ← fixed, no more 'root'
                    return raw.replaceAll("(?s)```json\\s*|```", "").trim(); // ← clean and return
                }
            }
            throw new ApiException("Failed to get response from AI");

        } catch (Exception e) {
            throw new ApiException("AI evaluation failed: " + e.getMessage());
        }
    }

    public String extractBillDataFromImage(MultipartFile file) {
        try {
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            String mediaType = file.getContentType() != null ? file.getContentType() : "image/jpeg";

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", anthropicApiKey);
            headers.set("anthropic-version", "2023-06-01");

            Map<String, Object> imageSource = Map.of(
                    "type", "base64",
                    "media_type", mediaType,
                    "data", base64Image
            );

            Map<String, Object> imageContent = Map.of("type", "image", "source", imageSource);
            Map<String, Object> textContent = Map.of("type", "text", "text",
                    "Extract bill data from this image and return ONLY a JSON object with these fields: " +
                    "type (must be exactly WATER, ELECTRICITY, or GAS), " +
                    "billMonth (format YYYY-MM-DD, use first day of the month), " +
                    "consumption (integer), unit (string), amount (decimal, total amount due), " +
                    "isInstallment (boolean), totalInstallment (integer, 0 if none), " +
                    "paidInstallment (integer, 0 if none), status (PENDING or PAID), imageUrl (empty string). " +
                    "Return raw JSON only, no explanation."
            );

            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("model", "claude-sonnet-4-5");
            requestBody.put("max_tokens", 500);
            requestBody.put("messages", List.of(
                    Map.of("role", "user", "content", List.of(imageContent, textContent))
            ));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.anthropic.com/v1/messages", request, Map.class);

            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("content")) {
                List<Map<String, Object>> content = (List<Map<String, Object>>) body.get("content");
                if (!content.isEmpty()) {
                    return ((String) content.get(0).get("text")).replaceAll("(?s)```json\\s*|```", "").trim();
                }
            }
            throw new ApiException("Failed to extract bill data from image");
        } catch (IOException e) {
            throw new ApiException("Failed to read image file: " + e.getMessage());
        }
    }

    public String extractPurchaseInvoiceDataFromImage(MultipartFile file) {
        try {
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            String mediaType = file.getContentType() != null ? file.getContentType() : "image/jpeg";

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", anthropicApiKey);
            headers.set("anthropic-version", "2023-06-01");

            Map<String, Object> imageSource = Map.of(
                    "type", "base64",
                    "media_type", mediaType,
                    "data", base64Image
            );

            Map<String, Object> imageContent = Map.of("type", "image", "source", imageSource);
            Map<String, Object> textContent = Map.of("type", "text", "text",
                    "Extract purchase invoice data from this receipt/invoice image and return ONLY a JSON object with these fields: " +
                    "productName (string), store (string), amount (decimal number), " +
                    "purchaseDate (format YYYY-MM-DD), category (e.g. Electronics, Furniture, Appliances, Food, Other), " +
                    "imageUrl (empty string), warrantyNote (string or null), warrantyExpiry (format YYYY-MM-DD or null). " +
                    "Return raw JSON only, no explanation."
            );

            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("model", "claude-sonnet-4-5");
            requestBody.put("max_tokens", 500);
            requestBody.put("messages", List.of(
                    Map.of("role", "user", "content", List.of(imageContent, textContent))
            ));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.anthropic.com/v1/messages", request, Map.class);

            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("content")) {
                List<Map<String, Object>> content = (List<Map<String, Object>>) body.get("content");
                if (!content.isEmpty()) {
                    return ((String) content.get(0).get("text")).replaceAll("(?s)```json\\s*|```", "").trim();
                }
            }
            throw new ApiException("Failed to extract invoice data from image");
        } catch (IOException e) {
            throw new ApiException("Failed to read image file: " + e.getMessage());
        }
    }

    public String generateMonthlyReportSummary(String homeAddress, int year, int month, String billsData, String purchasesData) {
        String prompt = String.format(
            "You are a smart home assistant for DAR platform. Write a concise monthly report in English for the home at: %s\n" +
            "Period: %d/%d\n\n" +
            "Bills Data:\n%s\n\n" +
            "Purchases Data:\n%s\n\n" +
            "Structure the report in exactly 4 short paragraphs with these plain labels (no markdown, no symbols):\n" +
            "Consumption Summary: one sentence about utility usage.\n" +
            "Expenses Summary: one sentence about purchases.\n" +
            "Observations: one sentence about anything unusual.\n" +
            "Recommendations: three short bullet points starting with a dash (-).\n" +
            "Keep it brief and easy to read.",
            homeAddress, month, year, billsData, purchasesData
        );
        return callClaudeApiText(prompt);
    }

    public String generateAnomalyExplanation(String billType, int currentConsumption, double avgConsumption, String unit) {
        String typeAr = switch (billType.toUpperCase()) {
            case "ELECTRICITY" -> "الكهرباء";
            case "WATER" -> "الماء";
            case "GAS" -> "الغاز";
            default -> billType;
        };
        int percentage = (int) Math.round(((currentConsumption - avgConsumption) / avgConsumption) * 100);

        String prompt = String.format(
            "فاتورة %s لمنزل ارتفع استهلاكها بنسبة %d%% عن المتوسط.\n" +
            "الاستهلاك الحالي: %d %s | المتوسط: %d %s\n\n" +
            "اكتب رسالة إشعار قصيرة وودية باللغة العربية لصاحب المنزل:\n" +
            "- اذكر السبب المحتمل للارتفاع (مثل: فصل الصيف، تسريب، أجهزة جديدة)\n" +
            "- أعطِ توصية عملية واحدة\n" +
            "- لا تتجاوز 3 جمل\n" +
            "- لا تبدأ بـ 'مرحبًا' ولا تضف تحيات",
            typeAr, percentage, currentConsumption, unit, (int) avgConsumption, unit
        );
        return callClaudeApiText(prompt);
    }

    public String generateWhatsAppMessage(String topic, String tone, String language) {
        String prompt = String.format(
                "Generate a WhatsApp message about: %s\n" +
                        "Tone: %s\n" +
                        "Language: %s\n\n" +
                        "Requirements:\n" +
                        "- Keep it concise and suitable for WhatsApp\n" +
                        "- Use appropriate emojis\n" +
                        "- Make it personal and engaging\n" +
                        "- Return ONLY the message text, nothing else",
                topic, tone, language
        );
        return callClaudeApi(prompt);
    }

    public String generateHomeItemMaintenanceAdvice(HomeItem homeItem) {
        String prompt = "اكتب نصائح صيانة قصيرة باللغة العربية لهذا الجهاز. " +
                "استخدم لغة بسيطة ومباشرة. أعد 3 نقاط فقط. " +
                "Device name: " + homeItem.getName() + ". " +
                "Category: " + homeItem.getCategory() + ". " +
                "Brand: " + homeItem.getBrand() + ". " +
                "Model: " + homeItem.getModel() + ". " +
                "Location: " + homeItem.getLocation() + ". " +
                "Install date: " + homeItem.getInstallDate() + ". " +
                "Lifespan months: " + homeItem.getLifespanMonth() + ". " +
                "Status: " + homeItem.getStatus() + ". " +
                "Next service date: " + homeItem.getNextServiceDate() + ". " +
                "Notes: " + homeItem.getNotes() + ".";

        return callClaudeApiText(prompt);
    }

}
