package org.dev.server.dto.trade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TradeRequestDto(
        UUID buyerId,
        UUID sellerId,
        Long buyOrderId,
        Long sellOrderId,
        Long assetId,
        BigDecimal price,
        BigDecimal quantity,
        LocalDateTime executedAt
) {}
