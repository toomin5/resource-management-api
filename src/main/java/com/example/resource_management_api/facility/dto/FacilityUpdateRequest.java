package com.example.resource_management_api.facility.dto;

import com.example.resource_management_api.facility.FacilityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityUpdateRequest {
    @NotBlank
    private String name;

    @NotNull
    private FacilityType type;

    private String description;
}
