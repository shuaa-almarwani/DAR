package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.Out.LocationDTOOut;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class NominatimService {

    private final RestTemplate restTemplate = new RestTemplate();

    public LocationDTOOut geocode(String address) {
        String url = UriComponentsBuilder
                .fromUriString("https://nominatim.openstreetmap.org/search")
                .queryParam("q", address)
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .toUriString();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "DAR/1.0");

            ResponseEntity<List> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    List.class
            );

            List<Map> response = responseEntity.getBody();
            if (response == null || response.isEmpty()) {
                throw new ApiException("Location not found");
            }

            Map location = response.get(0);
            String displayName = (String) location.get("display_name");
            Double latitude = Double.parseDouble((String) location.get("lat"));
            Double longitude = Double.parseDouble((String) location.get("lon"));

            return new LocationDTOOut(displayName, latitude, longitude);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Could not fetch location");
        }
    }
}
