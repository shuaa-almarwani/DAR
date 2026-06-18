package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.Out.NearbyPlaceDTOOut;
import com.example.DAR.Enums.HomeItemCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OverpassService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String OVERPASS_URL = "https://overpass-api.de/api/interpreter";

    public List<NearbyPlaceDTOOut> getNearbyMaintenancePlaces(Double latitude, Double longitude, HomeItemCategory category) {
        String query = buildQuery(latitude, longitude, category);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("data", query);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(OVERPASS_URL, new HttpEntity<>(body, headers), Map.class);
            return mapResponse(response.getBody(), latitude, longitude);
        } catch (Exception e) {
            throw new ApiException("Could not fetch nearby maintenance places");
        }
    }

    private String buildQuery(Double latitude, Double longitude, HomeItemCategory category) {
        String tags = getTagsByCategory(category);

        return "[out:json][timeout:25];" +
                "(" +
                tags.replace("{lat}", latitude.toString()).replace("{lon}", longitude.toString()) +
                ");" +
                "out center 10;";
    }

    private String getTagsByCategory(HomeItemCategory category) {
        return switch (category) {
            case AC -> "node[\"shop\"=\"electronics\"](around:5000,{lat},{lon});way[\"shop\"=\"electronics\"](around:5000,{lat},{lon});";
            case KITCHEN, WASHING -> "node[\"shop\"=\"appliance\"](around:5000,{lat},{lon});way[\"shop\"=\"appliance\"](around:5000,{lat},{lon});";
            case LIGHTING -> "node[\"shop\"=\"hardware\"](around:5000,{lat},{lon});way[\"shop\"=\"hardware\"](around:5000,{lat},{lon});";
            case SECURITY -> "node[\"shop\"=\"electronics\"](around:5000,{lat},{lon});node[\"craft\"=\"locksmith\"](around:5000,{lat},{lon});";
            case OTHER -> "node[\"shop\"=\"hardware\"](around:5000,{lat},{lon});way[\"shop\"=\"hardware\"](around:5000,{lat},{lon});";
        };
    }

    private List<NearbyPlaceDTOOut> mapResponse(Map responseBody, Double homeLatitude, Double homeLongitude) {
        List<NearbyPlaceDTOOut> places = new ArrayList<>();
        if (responseBody == null || responseBody.get("elements") == null) {
            return places;
        }

        List<Map> elements = (List<Map>) responseBody.get("elements");
        for (Map element : elements) {
            Map tags = (Map) element.get("tags");
            if (tags == null) {
                continue;
            }

            String name = (String) tags.getOrDefault("name", "Unknown place");
            String type = (String) tags.getOrDefault("shop", tags.getOrDefault("craft", "service"));
            Double lat = getCoordinate(element, "lat", "center", "lat");
            Double lon = getCoordinate(element, "lon", "center", "lon");
            if (lat == null || lon == null) {
                continue;
            }

            Double distance = Math.round((calculateDistanceInMeters(homeLatitude, homeLongitude, lat, lon) / 1000) * 10.0) / 10.0;
            places.add(new NearbyPlaceDTOOut(name, type, lat, lon, distance));
        }
        places.sort(Comparator.comparing(NearbyPlaceDTOOut::getDistanceInKm));
        return places;
    }

    private Double getCoordinate(Map element, String directKey, String centerKey, String centerCoordinateKey) {
        Object directValue = element.get(directKey);
        if (directValue instanceof Number number) {
            return number.doubleValue();
        }

        Map center = (Map) element.get(centerKey);
        if (center != null && center.get(centerCoordinateKey) instanceof Number number) {
            return number.doubleValue();
        }

        return null;
    }

    private Double calculateDistanceInMeters(Double lat1, Double lon1, Double lat2, Double lon2) {
        double earthRadius = 6371000;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
