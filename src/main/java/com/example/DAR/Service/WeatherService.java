package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherService {
    @Value("${weather.api.key}")
    private String weatherApiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    public String getWeatherDescription(String city){
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + city + "&appid=" + weatherApiKey + "&units=metric&lang=en";

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map body = response.getBody();

            Map main = (Map) body.get("main");
            List<Map> weatherList = (List<Map>) body.get("weather");

            double temp = (double) main.get("temp");
            String description = (String) weatherList.get(0).get("description");
            String cityName = (String) body.get("name");

            return String.format("City: %s, Temperature: %.1f°C, Condition: %s", cityName, temp, description);

        } catch (Exception e) {
            throw new ApiException("Could not fetch weather for city: " + city);
        }
    }

    public Map<String, Object> getWeatherData(String city) {

        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + city + "&appid=" + weatherApiKey + "&units=metric&lang=en";

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map body = response.getBody();

            if (body == null) {
                throw new ApiException("Weather data not found");
            }

            Map main = (Map) body.get("main");
            List<Map> weatherList = (List<Map>) body.get("weather");

            double temp = ((Number) main.get("temp")).doubleValue();
            int humidity = ((Number) main.get("humidity")).intValue();
            String description = (String) weatherList.get(0).get("description");
            String cityName = (String) body.get("name");

            Map<String, Object> weatherData = new HashMap<>();

            weatherData.put("city", cityName);
            weatherData.put("temperature", temp);
            weatherData.put("humidity", humidity);
            weatherData.put("description", description);

            return weatherData;

        } catch (Exception e) {
            throw new ApiException("Could not fetch weather for city: " + city);
        }
    }
    }


