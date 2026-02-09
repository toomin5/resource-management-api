package com.example.resource_management_api.search;

import com.example.resource_management_api.asset.dto.AssetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/assets")
    public ResponseEntity<List<AssetResponse>> searchAssets(@RequestParam String query) {
        List<AssetResponse> assets = searchService.searchAssets(query);
        return ResponseEntity.ok(assets);
    }
}
