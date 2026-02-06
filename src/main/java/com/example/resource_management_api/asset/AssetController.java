package com.example.resource_management_api.asset;

import com.example.resource_management_api.asset.dto.AssetCreateRequest;
import com.example.resource_management_api.asset.dto.AssetResponse;
import com.example.resource_management_api.facility.Facility;
import com.example.resource_management_api.facility.FacilityRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assets")
public class AssetController {

    private final AssetRepository assetRepository;
    private final FacilityRepository facilityRepository;

    @PostMapping
    public AssetResponse createAsset(
            @RequestBody @Valid AssetCreateRequest request
    ) {
        Facility facility = facilityRepository.findById(request.getFacilityId())
                .orElseThrow(() -> new IllegalArgumentException("Facility not found"));

        Asset asset = Asset.builder()
                .facility(facility)
                .name(request.getName())
                .category(request.getCategory())
                .currentStatus(request.getStatus())
                .build();

        Asset saved = assetRepository.save(asset);
        return new AssetResponse(saved);
    }

    @GetMapping("/{id}")
    public AssetResponse getAsset(@PathVariable Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found"));

        return new AssetResponse(asset);
    }

    @GetMapping
    public List<AssetResponse> getAssets() {
        return assetRepository.findAll()
                .stream()
                .map(AssetResponse::new)
                .toList();
    }
}