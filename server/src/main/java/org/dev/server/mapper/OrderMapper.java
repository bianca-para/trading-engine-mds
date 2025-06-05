package org.dev.server.mapper;

import org.dev.server.dto.order.OrderRequestDto;
import org.dev.server.dto.order.OrderResponseDto;
import org.dev.server.dto.order.PythonOrderRequestoDto;
import org.dev.server.model.Asset;
import org.dev.server.model.Order;
import org.dev.server.model.User;
import org.dev.server.model.enums.OrderStatus;

public class OrderMapper {
    public static Order toEntity(OrderRequestDto orderRequestDto, User user, Asset asset) {
        return new Order(
                user,
                asset,
                orderRequestDto.price(),
                orderRequestDto.quantity(),
                OrderStatus.OPEN,
                orderRequestDto.type(),
                orderRequestDto.createdAt()
        );
    }
    public static OrderResponseDto toDto(Order order) {
        return new OrderResponseDto(
                order.getOrderId(),
                order.getUser().getId(),
                order.getAsset().getId(),
                order.getQuantity(),
                order.getPrice(),
                order.getType(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
    public static PythonOrderRequestoDto toPythonRequestDto(Order order) {
        return new PythonOrderRequestoDto(
                order.getOrderId(),
                order.getUser().getId().toString(),
                order.getAsset().getSymbol(),
                order.getPrice(),
                order.getQuantity(),
                order.getType().toString(),
                order.getCreatedAt().getHour()
        );
    }
}
