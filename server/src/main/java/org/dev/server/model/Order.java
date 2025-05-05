package org.dev.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dev.server.model.enums.OrderStatus;
import org.dev.server.model.enums.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "Order must be associated with a user.")
    private User user;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    @NotNull(message = "Order must be associated with an asset.")
    private Asset asset;

    @NotNull(message = "Price can't be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Quantity can't be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than 0.")
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Order status can't be null.")
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Order type can't be null.")
    private OrderType type;

    @NotNull(message = "Order creation time can't be null.")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Order(User user, Asset asset, BigDecimal price, BigDecimal quantity, OrderStatus status, OrderType type, LocalDateTime createdAt) {
        this.user = user;
        this.asset = asset;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.type = type;
        this.createdAt = createdAt;
    }
}
