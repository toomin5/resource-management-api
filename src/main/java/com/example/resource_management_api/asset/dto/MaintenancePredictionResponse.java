package com.example.resource_management_api.asset.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MaintenancePredictionResponse {
    private Long assetId;
    private LocalDate predictedMaintenanceDate;
    private String confidence;
    private String analysisDetails;
}
