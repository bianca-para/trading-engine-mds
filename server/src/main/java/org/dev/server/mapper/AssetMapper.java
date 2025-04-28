package org.dev.server.mapper;

import org.dev.server.dto.asset.AssetRequestDto;
import org.dev.server.dto.asset.AssetResponseDto;
import org.dev.server.model.Asset;

public class AssetMapper {
    public static Asset toEntity(AssetRequestDto assetRequestDto) {
        return new Asset(
                assetRequestDto.symbol(),
                assetRequestDto.name(),
                assetRequestDto.price()
        );
    }

    public static AssetResponseDto toDto(Asset asset){
        return new AssetResponseDto(
                asset.getId(),
                asset.getSymbol(),
                asset.getName(),
                asset.getPrice()
        );
    }
}
