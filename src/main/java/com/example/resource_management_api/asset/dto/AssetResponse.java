package com.example.resource_management_api.asset.dto;

import com.example.resource_management_api.asset.Asset;
import com.example.resource_management_api.asset.AssetCategory;
import com.example.resource_management_api.asset.AssetStatus;
import lombok.Getter;

@Getter
public class AssetResponse {

    private Long id;
    private String name;
    private AssetCategory category;
    private AssetStatus status;
    private Long facilityId;

    public AssetResponse(Asset asset) {
        this.id = asset.getId();
        this.name = asset.getName();
        this.category = asset.getCategory();
        this.status = asset.getCurrentStatus();
        this.facilityId = asset.getFacility().getId();
    }
}
