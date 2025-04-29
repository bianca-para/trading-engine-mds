package org.dev.server.service.impl;

import org.dev.server.model.Trade;
import org.dev.server.repository.TradeRepository;
import org.dev.server.service.TradeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;

    public TradeServiceImpl(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @Override
    public Trade createTrade(Trade trade) {
        return tradeRepository.save(trade);
    }
    @Override
    public List<Trade> findByBuyer_Id(UUID buyerId) {
        return tradeRepository.findByBuyer_Id(buyerId);
    }

    @Override
    public List<Trade> findBySeller_Id(UUID sellerId) {
        return tradeRepository.findBySeller_Id(sellerId);
    }

    @Override
    public List<Trade> findByAssetSymbol(String symbol) {
        return tradeRepository.findByAssetSymbol(symbol);
    }

    @Override
    public List<Trade> findByExecutedAtBetween(LocalDateTime from, LocalDateTime to) {
        return tradeRepository.findByExecutedAtBetween(from, to);
    }

    @Override
    public List<Trade> findByPriceGreaterThan(BigDecimal minPrice) {
        return tradeRepository.findByPriceGreaterThan(minPrice);
    }
}