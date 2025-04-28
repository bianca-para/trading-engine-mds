package org.dev.server.dto.order;

import org.apache.catalina.manager.StatusTransformer;
import org.dev.server.model.enums.OrderStatus;
import org.dev.server.model.enums.OrderType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponseDto(
        Long orderId,
        UUID userId,
        Long assetId,
        BigDecimal quantity,
        BigDecimal price,
        OrderType orderType,
        OrderStatus status,
        LocalDateTime createdAt
) {
}
