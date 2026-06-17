package com.example.DAR.DTO.Out;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceSummaryDTOOut {

    private Integer totalMaintenances;
    private Integer upcomingMaintenances;
    private Integer overdueMaintenances;
    private Integer doneMaintenances;
    private Integer urgentMaintenances;
    private Double totalCost;
}