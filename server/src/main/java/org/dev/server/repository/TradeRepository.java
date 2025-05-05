package org.dev.server.repository;

import org.dev.server.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public interface TradeRepository extends JpaRepository<Trade, UUID> {
    //toate trade urile pentru un anumit buyer
    List<Trade> findByBuyer_Id(UUID buyerId);

    //toate trade urile pentru un anumit seller
    List<Trade> findBySeller_Id(UUID sellerId);

    //toate trade urile pentru o anumita moneda
    List<Trade> findByAssetSymbol(String symbol);

    //istoric intr un interval de timp
    List<Trade> findByExecutedAtBetween(LocalDateTime from, LocalDateTime to);

    //trade urile peste un anumit pret
    List<Trade> findByPriceGreaterThan(BigDecimal minPrice);
}
