package org.dev.server.service;

import org.dev.server.dto.asset.AssetRequestDto;
import org.dev.server.dto.asset.AssetResponseDto;

import java.util.List;

public interface AssetService {
    AssetResponseDto createAsset(AssetRequestDto assetRequestDto);

    AssetResponseDto getAsset(Long id);

    List<AssetResponseDto> getAllAssets();

    AssetResponseDto updateAsset(Long id, AssetRequestDto assetRequestDto);

    void deleteAsset(Long id);
}
