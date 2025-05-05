package org.dev.server.service;

import org.dev.server.model.Trade;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TradeService {
    Trade createTrade(Trade trade);
    List<Trade> findByBuyer_Id(UUID buyerId);
    List<Trade> findBySeller_Id(UUID sellerId);
    List<Trade> findByAssetSymbol(String symbol);
    List<Trade> findByExecutedAtBetween(LocalDateTime from, LocalDateTime to);
    List<Trade> findByPriceGreaterThan(BigDecimal minPrice);
}