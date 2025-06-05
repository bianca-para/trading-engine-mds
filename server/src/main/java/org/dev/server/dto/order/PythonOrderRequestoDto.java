package org.dev.server.dto.order;

import java.math.BigDecimal;

public record PythonOrderRequestoDto(
    Long id,
    String traderId,
    String symbol,
    BigDecimal price,
    BigDecimal quantity,
    String side,
    Integer timestamp
) {}
