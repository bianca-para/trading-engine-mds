//package org.dev.server.model;
//
//@Entity
//@Table(name = "trade")
//public class Trade {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long tradeId;
//
//    @ManyToOne
//    @JoinColumn(name = "buyer_id")
//    private User buyer;
//
//    @ManyToOne
//    @JoinColumn(name = "seller_id")
//    private User seller;
//
//    @ManyToOne
//    @JoinColumn(name = "buy_order_id")
//    private Order buyOrder;
//
//    @ManyToOne
//    @JoinColumn(name = "sell_order_id")
//    private Order sellOrder;
//
//    @ManyToOne
//    @JoinColumn(name = "asset_id")
//    private Asset asset;
//
//    private BigDecimal price;
//    private BigDecimal quantity;
//    private LocalDateTime executedAt;
//
//}