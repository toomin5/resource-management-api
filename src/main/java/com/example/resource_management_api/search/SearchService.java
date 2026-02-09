package com.example.resource_management_api.search;

import com.example.resource_management_api.asset.Asset;
import com.example.resource_management_api.asset.AssetRepository;
import com.example.resource_management_api.asset.dto.AssetResponse;
import com.example.resource_management_api.facility.Facility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Join;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final AssetRepository assetRepository;
    private final ObjectMapper objectMapper;

    /**
     * 자연어 쿼리를 구조화된 필터로 변환하기 위해 LLM API를 호출하기
     * 실제 애플리케이션에서는 외부 LLM 서비스에 대한 HTTP 호출 필요
     * 현재는 예제 쿼리를 기반으로 하드코딩된 JSON 문자열을 반환
     */
    private String callLlmApi(String naturalLanguageQuery) {

        if (naturalLanguageQuery.toLowerCase().contains("broken sensors in building a")) {
            return "{\"currentStatus\":\"ERROR\",\"category\":\"SENSOR\",\"facilityName\":\"Building A\"}";
        } else if (naturalLanguageQuery.toLowerCase().contains("active it equipment")) {
            return "{\"currentStatus\":\"ACTIVE\",\"category\":\"IT_EQUIPMENT\"}";
        } else if (naturalLanguageQuery.toLowerCase().contains("all assets")) {
             return "{}";
        }
        return "{}";
    }

    public List<AssetResponse> searchAssets(String naturalLanguageQuery) {
        String llmResponseJson = callLlmApi(naturalLanguageQuery);
        SearchFilterRequest filters;
        try {
            filters = objectMapper.readValue(llmResponseJson, SearchFilterRequest.class);
        } catch (JsonProcessingException e) {

            throw new RuntimeException("Failed to parse LLM response: " + e.getMessage());
        }

        Specification<Asset> spec = buildSpecification(filters);
        List<Asset> assets = assetRepository.findAll(spec);

        return assets.stream()
                .map(AssetResponse::new)
                .collect(Collectors.toList());
    }

    private Specification<Asset> buildSpecification(SearchFilterRequest filters) {
        return (root, query, criteriaBuilder) -> {

            List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();

            if (StringUtils.hasText(filters.getName())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filters.getName().toLowerCase() + "%"));
            }
            if (filters.getCategory() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), filters.getCategory()));
            }
            if (filters.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("currentStatus"), filters.getStatus()));
            }
            if (StringUtils.hasText(filters.getFacilityName())) {
                Join<Asset, Facility> facilityJoin = root.join("facility");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(facilityJoin.get("name")), "%" + filters.getFacilityName().toLowerCase() + "%"));
            }


            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}