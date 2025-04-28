package org.dev.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dev.server.dto.asset.AssetRequestDto;
import org.dev.server.dto.asset.AssetResponseDto;
import org.dev.server.service.AssetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @PostMapping
    public ResponseEntity<AssetResponseDto> createAsset(@Valid @RequestBody AssetRequestDto assetRequestDto) {
        AssetResponseDto createdAsset = assetService.createAsset(assetRequestDto);
        return new ResponseEntity<>(createdAsset, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AssetResponseDto>> getAllAssets() {
        List<AssetResponseDto> assets = assetService.getAllAssets();
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponseDto> getAsset(@PathVariable Long id) {
        AssetResponseDto asset = assetService.getAsset(id);
        return ResponseEntity.ok(asset);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetResponseDto> updateAsset(
            @PathVariable Long id,
            @Valid @RequestBody AssetRequestDto assetRequestDto) {
        AssetResponseDto updatedAsset = assetService.updateAsset(id, assetRequestDto);
        return ResponseEntity.ok(updatedAsset);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
}
