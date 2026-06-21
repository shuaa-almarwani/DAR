package com.example.DAR.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorReportDtoOut {

    private String homeAddress;
    private int totalSensors;
    private int activeSensors;
    private List<SensorStatDtoOut> sensors;
    private String aiSummary;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SensorStatDtoOut {
        private Integer sensorId;
        private String type;
        private String location;
        private Boolean isActive;
        private String unit;
        private Double avgValue;
        private Double minValue;
        private Double maxValue;
        private Integer readingCount;
        private Double latestValue;
    }
}
