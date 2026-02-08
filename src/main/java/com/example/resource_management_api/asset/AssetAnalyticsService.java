package com.example.resource_management_api.asset;

import com.example.resource_management_api.asset.dto.MaintenancePredictionResponse;
import com.example.resource_management_api.asset.history.AssetStatusHistory;
import com.example.resource_management_api.asset.history.AssetStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetAnalyticsService {

    private final AssetRepository assetRepository;
    private final AssetStatusHistoryRepository historyRepository;

    public MaintenancePredictionResponse predictNextMaintenance(Long assetId) {
        assetRepository.findById(assetId)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found with id: " + assetId));

        List<AssetStatusHistory> failureHistories = historyRepository.findByAssetIdOrderByChangedAtDesc(assetId)
                .stream()
                .filter(history -> history.getToStatus() == AssetStatus.ERROR || history.getToStatus() == AssetStatus.MAINTENANCE)
                .sorted(Comparator.comparing(AssetStatusHistory::getChangedAt).reversed()) // Ensure descending order
                .collect(Collectors.toList());

        if (failureHistories.size() < 2) {
            return MaintenancePredictionResponse.builder()
                    .assetId(assetId)
                    .confidence("LOW")
                    .analysisDetails("Not enough failure data to establish a reliable pattern (found " + failureHistories.size() + " records).")
                    .build();
        }

        long totalDaysBetweenFailures = 0;
        for (int i = 0; i < failureHistories.size() - 1; i++) {
            LocalDate date1 = failureHistories.get(i).getChangedAt().toLocalDate();
            LocalDate date2 = failureHistories.get(i + 1).getChangedAt().toLocalDate();
            totalDaysBetweenFailures += ChronoUnit.DAYS.between(date2, date1);
        }

        long averageIntervalDays = totalDaysBetweenFailures / (failureHistories.size() - 1);

        if (averageIntervalDays <= 0) {
             return MaintenancePredictionResponse.builder()
                    .assetId(assetId)
                    .confidence("LOW")
                    .analysisDetails("Failures occurred too close together to predict a future interval.")
                    .build();
        }

        LocalDate lastFailureDate = failureHistories.get(0).getChangedAt().toLocalDate();
        LocalDate predictedDate = lastFailureDate.plusDays(averageIntervalDays);

        String confidence = "MEDIUM";
        if (failureHistories.size() >= 5) {
            confidence = "HIGH";
        }

        String details = "Based on " + failureHistories.size() + " previous failure/maintenance records with an average interval of " + averageIntervalDays + " days.";

        return MaintenancePredictionResponse.builder()
                .assetId(assetId)
                .predictedMaintenanceDate(predictedDate)
                .confidence(confidence)
                .analysisDetails(details)
                .build();
    }
}
