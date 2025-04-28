package org.dev.server.dto.order;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.dev.server.model.enums.OrderStatus;
import org.dev.server.model.enums.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public record OrderRequestDto(
        @NotNull(message = "User ID cannot be null")
        UUID userId,

        @NotNull(message = "Asset ID cannot be null")
        Long assetId,

        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "Quantity cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than 0")
        BigDecimal quantity,

        @NotNull(message = "Order type cannot be null")
        OrderType type,


        @NotNull(message = "Order creation time cannot be null")
        LocalDateTime createdAt
) {
}
