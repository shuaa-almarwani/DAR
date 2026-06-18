package com.example.DAR.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearbyPlaceDTOOut {
    private String name;
    private String type;
    private Double latitude;
    private Double longitude;
    private Double distanceInKm;
}
