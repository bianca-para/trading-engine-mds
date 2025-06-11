package org.dev.server.service;

import org.dev.server.dto.asset.AssetRequestDto;
import org.dev.server.dto.asset.AssetResponseDto;
import org.dev.server.dto.order.OrderRequestDto;
import org.dev.server.dto.order.OrderResponseDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

    OrderResponseDto cancelOrder(Long orderId);

    OrderResponseDto updateOrder(Long orderId, OrderRequestDto orderRequestDto);

    OrderResponseDto getOrderDetails(Long orderId);

    List<OrderResponseDto> getAllOrdersForUser(UUID userId);

    List<OrderResponseDto> getAllOrdersForAsset(Long assetId);
}
