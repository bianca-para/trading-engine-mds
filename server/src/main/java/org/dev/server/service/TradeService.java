package org.dev.server.service;

import org.dev.server.dto.trade.TradeRequestDto;
import org.dev.server.dto.trade.TradeResponseDto;
import org.dev.server.model.Trade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TradeService {
    TradeResponseDto createTrade(TradeRequestDto trade);
    TradeResponseDto getTrade(UUID tradeId);
    List<TradeResponseDto> findByBuyer_Id(UUID buyerId);
    List<TradeResponseDto> findBySeller_Id(UUID sellerId);
    List<TradeResponseDto> findByAssetSymbol(String symbol);
    List<TradeResponseDto> findByExecutedAtBetween(LocalDateTime from, LocalDateTime to);
    List<TradeResponseDto> findByPriceGreaterThan(BigDecimal minPrice);
}