package org.dev.server.unitTests.trade.service;

import net.datafaker.Faker;
import org.dev.server.dto.trade.TradeRequestDto;
import org.dev.server.dto.trade.TradeResponseDto;
import org.dev.server.exception.TradeAlreadyExistsException;
import org.dev.server.exception.TradeNotFoundException;
import org.dev.server.mapper.TradeMapper;
import org.dev.server.model.Asset;
import org.dev.server.model.Order;
import org.dev.server.model.Trade;
import org.dev.server.model.User;
import org.dev.server.repository.TradeRepository;
import org.dev.server.service.impl.TradeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTests {

    @Mock
    private TradeRepository repo;

    @InjectMocks
    private TradeServiceImpl service;

    private UUID tid, buyerId, sellerId;
    private Long buyOrderId, sellOrderId, assetId;
    private BigDecimal price, quantity;
    private LocalDateTime executedAt;
    private TradeRequestDto request;
    private Trade entity;
    private TradeResponseDto expectedDto;

    @BeforeEach
    void initTestData() {
        Faker faker = new Faker();

        // common test data
        tid         = UUID.randomUUID();
        buyerId     = UUID.randomUUID();
        sellerId    = UUID.randomUUID();
        buyOrderId  = 100L;
        sellOrderId = 200L;
        assetId     = 1L;
        price       = BigDecimal.valueOf(faker.number().randomDouble(2, 1, 1000));
        quantity    = BigDecimal.valueOf(faker.number().randomDouble(2, 1, 10));
        executedAt  = LocalDateTime.now();

        // build request DTO
        request = new TradeRequestDto(
                buyerId, sellerId,
                buyOrderId, sellOrderId,
                assetId,
                price, quantity, executedAt
        );

        // build entity with all fields populated
        entity = new Trade();
        entity.setTradeId(tid);
        User buyer = new User(); buyer.setId(buyerId);
        User seller = new User(); seller.setId(sellerId);
        Order buyOrder = new Order(); buyOrder.setOrderId(buyOrderId);
        Order sellOrder = new Order(); sellOrder.setOrderId(sellOrderId);
        Asset asset = new Asset(); asset.setId(assetId); asset.setSymbol("ASSET");

        entity.setBuyer(buyer);
        entity.setSeller(seller);
        entity.setBuyOrder(buyOrder);
        entity.setSellOrder(sellOrder);
        entity.setAsset(asset);
        entity.setPrice(price);
        entity.setQuantity(quantity);
        entity.setExecutedAt(executedAt);

        // prepare the expected DTO from the real mapper
        expectedDto = TradeMapper.toResponseDto(entity);
    }

    @Test
    void createTrade_shouldSaveAndReturn() {
        // stub exactly what this test needs:
        when(repo.existsByBuyOrder_OrderIdAndSellOrder_OrderId(buyOrderId, sellOrderId))
                .thenReturn(false);
        when(repo.save(any(Trade.class)))
                .thenReturn(entity);

        TradeResponseDto result = service.createTrade(request);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expectedDto);

        verify(repo).save(any(Trade.class));
    }

    @Test
    void createTrade_shouldThrowWhenAlreadyExists() {
        when(repo.existsByBuyOrder_OrderIdAndSellOrder_OrderId(buyOrderId, sellOrderId))
                .thenReturn(true);

        assertThrows(TradeAlreadyExistsException.class,
                () -> service.createTrade(request));

        verify(repo, never()).save(any());
    }

    @Test
    void getTrade_shouldReturnResponseIfFound() {
        when(repo.findById(tid)).thenReturn(Optional.of(entity));

        TradeResponseDto result = service.getTrade(tid);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expectedDto);

        verify(repo).findById(tid);
    }

    @Test
    void getTrade_shouldThrowIfNotFound() {
        when(repo.findById(tid)).thenReturn(Optional.empty());

        assertThrows(TradeNotFoundException.class,
                () -> service.getTrade(tid));

        verify(repo).findById(tid);
    }

    @Test
    void findByBuyer_Id_shouldReturnMappedList() {
        List<Trade> list = IntStream.range(0, 3)
                .mapToObj(i -> {
                    Trade t = new Trade();
                    t.setTradeId(UUID.randomUUID());
                    t.setBuyer(new User() {{ setId(buyerId); }});
                    t.setSeller(new User() {{ setId(sellerId); }});
                    t.setBuyOrder(new Order() {{ setOrderId(buyOrderId); }});
                    t.setSellOrder(new Order() {{ setOrderId(sellOrderId); }});
                    t.setAsset(new Asset() {{ setId(assetId); setSymbol("AS"); }});
                    t.setPrice(price);
                    t.setQuantity(quantity);
                    t.setExecutedAt(executedAt);
                    return t;
                })
                .collect(Collectors.toList());

        when(repo.findByBuyer_Id(buyerId)).thenReturn(list);

        List<TradeResponseDto> results = service.findByBuyer_Id(buyerId);

        assertThat(results).hasSize(3);
        assertThat(results.get(0))
                .usingRecursiveComparison()
                .isEqualTo(TradeMapper.toResponseDto(list.get(0)));

        verify(repo).findByBuyer_Id(buyerId);
    }

    @Test
    void findBySeller_Id_shouldReturnMappedList() {
        when(repo.findBySeller_Id(sellerId)).thenReturn(List.of(entity, entity));

        List<TradeResponseDto> results = service.findBySeller_Id(sellerId);

        assertThat(results).hasSize(2);
        verify(repo).findBySeller_Id(sellerId);
    }

    @Test
    void findByAssetSymbol_shouldReturnMappedList() {
        when(repo.findByAssetSymbol("ASSET")).thenReturn(List.of(entity));

        List<TradeResponseDto> results = service.findByAssetSymbol("ASSET");

        assertThat(results).hasSize(1);
        verify(repo).findByAssetSymbol("ASSET");
    }

    @Test
    void findByExecutedAtBetween_shouldReturnMappedList() {
        LocalDateTime from = executedAt.minusDays(1);
        LocalDateTime to   = executedAt.plusDays(1);

        when(repo.findByExecutedAtBetween(from, to))
                .thenReturn(List.of(entity));

        List<TradeResponseDto> results = service.findByExecutedAtBetween(from, to);

        assertThat(results).hasSize(1);
        verify(repo).findByExecutedAtBetween(from, to);
    }

    @Test
    void findByPriceGreaterThan_shouldReturnMappedList() {
        BigDecimal min = BigDecimal.valueOf(500);

        when(repo.findByPriceGreaterThan(min))
                .thenReturn(List.of(entity, entity, entity));

        List<TradeResponseDto> results = service.findByPriceGreaterThan(min);

        assertThat(results).hasSize(3);
        verify(repo).findByPriceGreaterThan(min);
    }
}
