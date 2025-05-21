package org.dev.server.dto.trade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TradeResponseDto(
        UUID tradeId,
        UUID buyerId,
        UUID sellerId,
        Long buyOrderId,
        Long sellOrderId,
        String assetSymbol,
        BigDecimal price,
        BigDecimal quantity,
        LocalDateTime executedAt
) {}
