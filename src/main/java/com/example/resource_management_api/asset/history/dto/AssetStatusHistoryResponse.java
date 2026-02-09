package com.example.resource_management_api.asset.history.dto;

import com.example.resource_management_api.asset.AssetStatus;
import com.example.resource_management_api.asset.history.AssetStatusHistory;
import com.example.resource_management_api.asset.history.StatusChangeReason;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AssetStatusHistoryResponse {
    private Long id;
    private Long assetId;
    private AssetStatus fromStatus;
    private AssetStatus toStatus;
    private Long changedByUserId;
    private String changedByUserName; // Added for convenience
    private LocalDateTime changedAt;
    private StatusChangeReason reason;

    public AssetStatusHistoryResponse(AssetStatusHistory history) {
        this.id = history.getId();
        this.assetId = history.getAsset().getId();
        this.fromStatus = history.getFromStatus();
        this.toStatus = history.getToStatus();
        this.changedByUserId = history.getChangedBy().getId();
        this.changedByUserName = history.getChangedBy().getName();
        this.changedAt = history.getChangedAt();
        this.reason = history.getReason();
    }
}
