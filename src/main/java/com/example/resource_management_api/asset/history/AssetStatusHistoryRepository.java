package com.example.resource_management_api.asset.history;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetStatusHistoryRepository
        extends JpaRepository<AssetStatusHistory, Long> {

    List<AssetStatusHistory> findByAssetIdOrderByChangedAtDesc(Long assetId);
}
