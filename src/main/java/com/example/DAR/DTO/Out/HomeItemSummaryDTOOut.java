package com.example.DAR.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomeItemSummaryDTOOut {
    private Integer totalItems;
    private Integer needsMaintenance;
    private Integer upcomingServiceCount;
    private Integer averageLifePercentage;
}
