package com.example.resource_management_api.facility.dto;

import com.example.resource_management_api.facility.Facility;
import com.example.resource_management_api.facility.FacilityType;
import lombok.Getter;

@Getter
public class FacilityResponse {

    private Long id;
    private String name;
    private FacilityType type;
    private String description;

    public FacilityResponse(Facility facility) {
        this.id = facility.getId();
        this.name = facility.getName();
        this.type = facility.getType();
        this.description = facility.getDescription();
    }
}
