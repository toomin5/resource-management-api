package com.example.resource_management_api.facility;

import com.example.resource_management_api.facility.dto.FacilityCreateRequest;
import com.example.resource_management_api.facility.dto.FacilityResponse;
import com.example.resource_management_api.facility.dto.FacilityUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FacilityResponse createFacility(
            @RequestBody @Valid FacilityCreateRequest request
    ) {
        Facility facility = facilityService.createFacility(request);
        return new FacilityResponse(facility);
    }

    @GetMapping("/{id}")
    public FacilityResponse getFacility(@PathVariable Long id) {
        Facility facility = facilityService.getFacilityById(id);
        return new FacilityResponse(facility);
    }

    @GetMapping
    public List<FacilityResponse> getFacilities() {
        return facilityService.getAllFacilities().stream() // Assuming getAllFacilities method in FacilityService
                .map(FacilityResponse::new)
                .toList();
    }

    @PutMapping("/{id}")
    public FacilityResponse updateFacility(@PathVariable Long id, @RequestBody @Valid FacilityUpdateRequest request) {
        Facility updatedFacility = facilityService.updateFacility(id, request);
        return new FacilityResponse(updatedFacility);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFacility(@PathVariable Long id) {
        facilityService.deleteFacility(id);
    }
}