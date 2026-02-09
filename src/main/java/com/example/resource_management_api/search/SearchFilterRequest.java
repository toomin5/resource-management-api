package com.example.resource_management_api.search;

import com.example.resource_management_api.asset.AssetCategory;
import com.example.resource_management_api.asset.AssetStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchFilterRequest {
    private String name;
    private AssetCategory category;
    private AssetStatus status;
    private String facilityName;
}
