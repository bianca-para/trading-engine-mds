package org.dev.server.controller;

import org.dev.server.model.Trade;
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
    public ResponseEntity<Trade> create(@RequestBody Trade trade) {
        return ResponseEntity.ok(service.createTrade(trade));
    }

    @GetMapping("/asset/{symbol}")
    public ResponseEntity<List<Trade>> byAsset(@PathVariable String symbol) {
        return ResponseEntity.ok(service.findByAssetSymbol(symbol));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Trade>> byBuyer(@PathVariable UUID buyerId) {
        return ResponseEntity.ok(service.findByBuyer_Id(buyerId));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Trade>> bySeller(@PathVariable UUID sellerId) {
        return ResponseEntity.ok(service.findBySeller_Id(sellerId));
    }

    @GetMapping("/executed")
    public ResponseEntity<List<Trade>> byExecutedBetween(
            @RequestParam("from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(service.findByExecutedAtBetween(from, to));
    }

    @GetMapping("/price-over/{minPrice}")
    public ResponseEntity<List<Trade>> byPriceOver(@PathVariable BigDecimal minPrice) {
        return ResponseEntity.ok(service.findByPriceGreaterThan(minPrice));
    }
}
