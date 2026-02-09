package com.example.resource_management_api.asset.dto;

import com.example.resource_management_api.asset.AssetCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetUpdateRequest {
    @NotNull
    private Long facilityId;

    @NotBlank
    private String name;

    @NotNull
    private AssetCategory category;
}
