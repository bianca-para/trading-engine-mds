//package org.dev.server.model;
//
//@Entity
//@Table(name = "orders")
//public class Order {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long orderId;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "asset_id")
//    private Asset asset;
//
//    private BigDecimal price;
//    private BigDecimal quantity;
//
//    @Enumerated(EnumType.STRING)
//    private OrderStatus status;
//
//    @Enumerated(EnumType.STRING)
//    private OrderType type;
//
//    private LocalDateTime createdAt;
//
//}
