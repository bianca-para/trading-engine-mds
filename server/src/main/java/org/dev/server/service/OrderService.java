package org.dev.server.service;

import org.dev.server.dto.asset.AssetRequestDto;
import org.dev.server.dto.asset.AssetResponseDto;
import org.dev.server.dto.order.OrderRequestDto;
import org.dev.server.dto.order.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

    OrderResponseDto cancelOrder(Long orderId);

    OrderResponseDto updateOrder(Long orderId, OrderRequestDto orderRequestDto);

    OrderResponseDto getOrderDetails(Long orderId);

    List<OrderResponseDto> getAllOrdersForUser(Long userId);
}
