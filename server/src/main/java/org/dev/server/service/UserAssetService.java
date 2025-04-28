package org.dev.server.service;

import org.dev.server.dto.UserResponseDto;
import org.dev.server.dto.asset.AssetResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface UserAssetService {

    void addAssetToUser(UUID userId, Long assetId, BigDecimal quantity);

    void removeAssetFromUser(UUID userId, Long assetId);

    void updateAssetQuantity(UUID userId, Long assetId, BigDecimal quantityChange);

    List<Long> getAssetsIdByUserId(UUID userId);

    List<UUID> getUsersIdByAssetId(Long assetId);

    List<AssetResponseDto> getAssetsByUserId(UUID userId);

    List<UserResponseDto> getUsersByAssetId(Long assetId);

    BigDecimal getUserQuantity(UUID userId, Long assetId);

}
