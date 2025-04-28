package org.dev.server.dto.asset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record AssetRequestDto(
        @NotBlank(message = "Symbol cannot be blank")
        @Size(max = 10, message = "Symbol cannot be longer than 10 characters")
        String symbol,

        @NotBlank(message = "Name cannot be blank")
        @Size(max = 50, message = "Name cannot be longer than 50 characters")
        String name,

        @PositiveOrZero(message = "Price must be zero or positive")
        Double price
) {
}
