package org.dev.server.controller;

import org.dev.server.dto.trade.TradeRequestDto;
import org.dev.server.dto.trade.TradeResponseDto;
import org.dev.server.service.TradeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trades")
public class TradeController {
    private final TradeService service;

    public TradeController(TradeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TradeResponseDto> create(@RequestBody TradeRequestDto tradeRequest) {
        return ResponseEntity.ok(service.createTrade(tradeRequest));
    }

    @GetMapping("/asset/{symbol}")
    public ResponseEntity<List<TradeResponseDto>> byAsset(@PathVariable String symbol) {
        return ResponseEntity.ok(service.findByAssetSymbol(symbol));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<TradeResponseDto>> byBuyer(@PathVariable UUID buyerId) {
        return ResponseEntity.ok(service.findByBuyer_Id(buyerId));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<TradeResponseDto>> bySeller(@PathVariable UUID sellerId) {
        return ResponseEntity.ok(service.findBySeller_Id(sellerId));
    }

    @GetMapping("/executed")
    public ResponseEntity<List<TradeResponseDto>> byExecutedBetween(
            @RequestParam("from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(service.findByExecutedAtBetween(from, to));
    }

    @GetMapping("/price-over/{minPrice}")
    public ResponseEntity<List<TradeResponseDto>> byPriceOver(@PathVariable BigDecimal minPrice) {
        return ResponseEntity.ok(service.findByPriceGreaterThan(minPrice));
    }
}