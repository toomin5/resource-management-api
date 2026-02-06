package com.example.resource_management_api.facility;

import com.example.resource_management_api.facility.dto.FacilityCreateRequest;
import com.example.resource_management_api.facility.dto.FacilityResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/facilities")
public class FacilityController {

    private final FacilityRepository facilityRepository;

    @PostMapping
    public FacilityResponse createFacility(
            @RequestBody @Valid FacilityCreateRequest request
    ) {
        Facility facility = Facility.builder()
                .name(request.getName())
                .type(request.getType())
                .description(request.getDescription())
                .build();

        Facility saved = facilityRepository.save(facility);
        return new FacilityResponse(saved);
    }

    @GetMapping("/{id}")
    public FacilityResponse getFacility(@PathVariable Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found"));

        return new FacilityResponse(facility);
    }

    @GetMapping
    public List<FacilityResponse> getFacilities() {
        return facilityRepository.findAll()
                .stream()
                .map(FacilityResponse::new)
                .toList();
    }
}