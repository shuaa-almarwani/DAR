package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class WorkflowTriggerService {

    private final RestClient restClient = RestClient.create();

    @Value("${n8n.webhook.sensor-analysis}")
    private String sensorAnalysisUrl;
    public String triggerSensorAnalysis(Integer sensorId) {
        try {
            Map response = restClient.get()
                    .uri(sensorAnalysisUrl + "?sensorId=" + sensorId)
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.containsKey("content")) {
                List<Map> content = (List<Map>) response.get("content");
                if (!content.isEmpty()) return (String) content.get(0).get("text");
            }
            throw new ApiException("No content returned from N8N");
        } catch (Exception e) {
            throw new ApiException("Failed to analyze sensor readings: " + e.getMessage());
        }
    }
    @Value("${n8n.webhook.sensor-connection}")
    private String sensorConnectionUrl;
    public String connectSensor (Integer sensorId){
        try {
            restClient.get()
                    .uri(sensorConnectionUrl + "?sensorId=" + sensorId)
                    .retrieve()
                    .body(Map.class);



        } catch (Exception e) {
            throw new ApiException("Failed to connect sensor : " + e.getMessage());
        }
        return "";
    }

}