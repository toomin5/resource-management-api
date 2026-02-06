package com.example.resource_management_api.asset.dto;

import com.example.resource_management_api.asset.AssetCategory;
import com.example.resource_management_api.asset.AssetStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AssetCreateRequest {

    @NotNull
    private Long facilityId;

    @NotBlank
    private String name;

    @NotNull
    private AssetCategory category;

    @NotNull
    private AssetStatus status;
}
