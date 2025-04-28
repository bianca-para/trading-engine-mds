package org.dev.server.controller;

import lombok.RequiredArgsConstructor;
import org.dev.server.dto.UserResponseDto;
import org.dev.server.dto.asset.AssetResponseDto;
import org.dev.server.model.UserAsset;
import org.dev.server.service.UserAssetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-asset")
@RequiredArgsConstructor
public class UserAssetController {

    private final UserAssetService userAssetService;

    @PostMapping
    public ResponseEntity<Void> addAssetToUser(
            @RequestParam UUID userId,
            @RequestParam Long assetId,
            @RequestParam BigDecimal quantity
    ) {
        userAssetService.addAssetToUser(userId, assetId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/quantity")
    public ResponseEntity<BigDecimal> getUserQuantity(
            @RequestParam UUID userId,
            @RequestParam Long assetId
    ) {
        BigDecimal quantity = userAssetService.getUserQuantity(userId, assetId);
        return ResponseEntity.ok(quantity);
    }



    @PostMapping("/quantity")
    public ResponseEntity<Void> updateAssetQuantity(
            @RequestParam UUID userId,
            @RequestParam Long assetId,
            @RequestParam BigDecimal quantityChange
    ) {
        userAssetService.updateAssetQuantity(userId, assetId, quantityChange);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeAssetFromUser(
            @RequestParam UUID userId,
            @RequestParam Long assetId
    ) {
        userAssetService.removeAssetFromUser(userId, assetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/assets/{userId}")
    public ResponseEntity<List<AssetResponseDto>> getAssetsByUserId(@PathVariable UUID userId) {
        List<AssetResponseDto> assets = userAssetService.getAssetsByUserId(userId);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/users/{assetId}")
    public ResponseEntity<List<UserResponseDto>> getUsersByAssetId(@PathVariable Long assetId) {
        List<UserResponseDto> users = userAssetService.getUsersByAssetId(assetId);
        return ResponseEntity.ok(users);
    }
}
