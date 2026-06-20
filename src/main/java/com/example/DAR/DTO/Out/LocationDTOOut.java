package com.example.DAR.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTOOut {
    private String displayName;
    private Double latitude;
    private Double longitude;
}
