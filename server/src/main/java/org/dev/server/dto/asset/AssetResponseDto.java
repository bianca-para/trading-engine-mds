package org.dev.server.dto.asset;

public record AssetResponseDto(
        Long assetId,
        String symbol,
        String name,
        Double price
) {
}
