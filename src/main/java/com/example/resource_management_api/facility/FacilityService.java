package com.example.resource_management_api.facility;

import com.example.resource_management_api.facility.dto.FacilityCreateRequest;
import com.example.resource_management_api.facility.dto.FacilityUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // Added import

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;

    @Transactional
    public Facility createFacility(FacilityCreateRequest request) {
        Facility facility = Facility.builder()
                .name(request.getName())
                .type(request.getType())
                .description(request.getDescription())
                .build();
        return facilityRepository.save(facility);
    }

    @Transactional(readOnly = true)
    public List<Facility> getAllFacilities() { // New method
        return facilityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Facility getFacilityById(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found with id: " + id));
    }

    @Transactional
    public Facility updateFacility(Long id, FacilityUpdateRequest request) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found with id: " + id));

        facility.setName(request.getName());
        facility.setType(request.getType());
        facility.setDescription(request.getDescription());

        return facilityRepository.save(facility);
    }

    @Transactional
    public void deleteFacility(Long id) {
        if (!facilityRepository.existsById(id)) {
            throw new IllegalArgumentException("Facility not found with id: " + id);
        }
        facilityRepository.deleteById(id);
    }
}
