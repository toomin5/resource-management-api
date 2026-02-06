package com.example.resource_management_api.asset.history;

public enum StatusChangeReason {
    MAINTENANCE_START,
    MAINTENANCE_END,
    FAILURE,
    RECOVERY,
    MANUAL_CHANGE
}