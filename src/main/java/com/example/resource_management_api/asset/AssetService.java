package com.example.resource_management_api.asset;

import com.example.resource_management_api.asset.dto.AssetStatusChangeRequest;
import com.example.resource_management_api.asset.dto.AssetUpdateRequest;
import com.example.resource_management_api.asset.history.AssetStatusHistory;
import com.example.resource_management_api.asset.history.AssetStatusHistoryRepository;
import com.example.resource_management_api.facility.Facility;
import com.example.resource_management_api.facility.FacilityRepository;
import com.example.resource_management_api.user.User;
import com.example.resource_management_api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetStatusHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository; // Inject FacilityRepository

    @Transactional
    public void changeAssetStatus(Long assetId, AssetStatusChangeRequest request) {

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found"));

        User user = userRepository.findById(request.getChangedByUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        AssetStatus fromStatus = asset.getCurrentStatus();
        AssetStatus toStatus = request.getToStatus();

        asset.changeStatus(toStatus);

        AssetStatusHistory history = AssetStatusHistory.builder()
                .asset(asset)
                .fromStatus(fromStatus)
                .toStatus(toStatus)
                .changedBy(user)
                .changedAt(LocalDateTime.now())
                .reason(request.getReason())
                .build();

        historyRepository.save(history);
    }

    @Transactional
    public Asset updateAsset(Long assetId, AssetUpdateRequest request) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found with id: " + assetId));

        Facility facility = facilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new IllegalArgumentException("Facility not found with id: " + request.getFacilityId()));

        asset.setName(request.getName());
        asset.setCategory(request.getCategory());
        asset.setFacility(facility);

        return assetRepository.save(asset);
    }

    @Transactional
    public void deleteAsset(Long assetId) {
        if (!assetRepository.existsById(assetId)) {
            throw new IllegalArgumentException("Asset not found with id: " + assetId);
        }
        assetRepository.deleteById(assetId);
    }
}
