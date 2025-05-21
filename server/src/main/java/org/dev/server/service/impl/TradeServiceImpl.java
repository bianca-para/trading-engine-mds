package org.dev.server.service.impl;

import org.dev.server.dto.trade.TradeRequestDto;
import org.dev.server.dto.trade.TradeResponseDto;
import org.dev.server.exception.TradeAlreadyExistsException;
import org.dev.server.exception.TradeNotFoundException;
import org.dev.server.mapper.TradeMapper;
import org.dev.server.model.Trade;
import org.dev.server.repository.TradeRepository;
import org.dev.server.service.TradeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TradeServiceImpl implements TradeService {
    private final TradeRepository tradeRepository;

    public TradeServiceImpl(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @Override
    public TradeResponseDto createTrade(TradeRequestDto tradeRequestDto) {
        boolean exists = tradeRepository.existsByBuyOrder_OrderIdAndSellOrder_OrderId(
                tradeRequestDto.buyOrderId(),
                tradeRequestDto.sellOrderId()
        );
        if(exists) {
            throw new TradeAlreadyExistsException("Trade with these buy and sell orders already exists");
        }
        Trade trade = TradeMapper.toEntity(tradeRequestDto);
        Trade savedTrade = tradeRepository.save(trade);
        return TradeMapper.toResponseDto(savedTrade);
    }

    @Override
    public List<TradeResponseDto> findByBuyer_Id(UUID buyerId) {
        return tradeRepository.findByBuyer_Id(buyerId).stream()
                .map(TradeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TradeResponseDto> findBySeller_Id(UUID sellerId) {
        return tradeRepository.findBySeller_Id(sellerId).stream()
                .map(TradeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TradeResponseDto> findByAssetSymbol(String symbol) {
        return tradeRepository.findByAssetSymbol(symbol).stream()
                .map(TradeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TradeResponseDto> findByExecutedAtBetween(LocalDateTime from, LocalDateTime to) {
        return tradeRepository.findByExecutedAtBetween(from, to).stream()
                .map(TradeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TradeResponseDto> findByPriceGreaterThan(BigDecimal minPrice) {
        return tradeRepository.findByPriceGreaterThan(minPrice).stream()
                .map(TradeMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    @Override
    public TradeResponseDto getTrade(UUID tradeId) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(()->new TradeNotFoundException("Trade with ID " + tradeId + " not found"));
        return TradeMapper.toResponseDto(trade);
    }


}