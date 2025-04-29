package org.dev.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID tradeId;

    @NotNull(message = "Buyer must not be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @NotNull(message = "Seller must not be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @NotNull(message = "Buy order must not be null")
    @ManyToOne
    @JoinColumn(name = "buy_order_id", nullable = false)
    private Order buyOrder;

    @NotNull(message = "Sell order must not be null")
    @ManyToOne
    @JoinColumn(name = "sell_order_id", nullable = false)
    private Order sellOrder;

    @NotNull(message = "Asset must not be null")
    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    @Column(name = "executed_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime executedAt;

    //constr fara id, generate direct in bd

    public Trade(User buyer,
                 User seller,
                 Order buyOrder,
                 Order sellOrder,
                 Asset asset,
                 BigDecimal price,
                 BigDecimal quantity,
                 LocalDateTime executedAt) {
        this.buyer = buyer;
        this.seller = seller;
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.asset = asset;
        this.price = price;
        this.quantity = quantity;
        this.executedAt = executedAt;
    }

}