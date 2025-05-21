package org.dev.server.mapper;

import org.dev.server.dto.trade.TradeRequestDto;
import org.dev.server.dto.trade.TradeResponseDto;
import org.dev.server.model.Trade;
import org.dev.server.model.User;
import org.dev.server.model.Order;
import org.dev.server.model.Asset;
import org.springframework.stereotype.Component;


@Component
public class TradeMapper {
    public static Trade toEntity(TradeRequestDto dto) {
        Trade t = new Trade();

        // creare useri doar cu id
        User buyer = new User();
        buyer.setId(dto.buyerId());

        User seller = new User();
        seller.setId(dto.sellerId());

        // creare comenzi doar cu id
        Order buyOrder = new Order();
        buyOrder.setOrderId(dto.buyOrderId());

        Order sellOrder = new Order();
        sellOrder.setOrderId(dto.sellOrderId());

        // creare asset uri doar cu id
        Asset asset = new Asset();
        asset.setId(dto.assetId());

        t.setBuyer(buyer);
        t.setSeller(seller);
        t.setBuyOrder(buyOrder);
        t.setSellOrder(sellOrder);
        t.setAsset(asset);
        t.setPrice(dto.price());
        t.setQuantity(dto.quantity());
        t.setExecutedAt(dto.executedAt());

        return t;
    }

    public static TradeResponseDto toResponseDto(Trade t) {
        return new TradeResponseDto(
                t.getTradeId(),
                t.getBuyer().getId(),
                t.getSeller().getId(),
                t.getBuyOrder().getOrderId(),
                t.getSellOrder().getOrderId(),
                t.getAsset().getSymbol(),
                t.getPrice(),
                t.getQuantity(),
                t.getExecutedAt()
        );
    }
}