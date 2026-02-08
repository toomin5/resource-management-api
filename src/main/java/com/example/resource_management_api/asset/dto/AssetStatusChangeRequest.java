package com.example.resource_management_api.asset.dto;

import com.example.resource_management_api.asset.AssetStatus;
import com.example.resource_management_api.asset.history.StatusChangeReason;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AssetStatusChangeRequest {

    @NotNull
    private AssetStatus toStatus;

    @NotNull
    private Long changedByUserId;

    private StatusChangeReason reason;
}