package com.example.resource_management_api.asset.history;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetStatusHistoryRepository extends JpaRepository<AssetStatusHistory, Long> {
}
