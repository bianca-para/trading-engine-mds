package org.dev.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.dev.server.dto.order.OrderRequestDto;
import org.dev.server.dto.order.OrderResponseDto;
import org.dev.server.mapper.OrderMapper;
import org.dev.server.model.Asset;
import org.dev.server.model.Order;
import org.dev.server.model.User;
import org.dev.server.model.enums.OrderStatus;
import org.dev.server.repository.AssetRepository;
import org.dev.server.repository.OrderRepository;
import org.dev.server.repository.UserRepository;
import org.dev.server.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        User user = userRepository.findById(orderRequestDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User with id " + orderRequestDto.userId() + " not found."));

        Asset asset = assetRepository.findById(orderRequestDto.assetId())
                .orElseThrow(() -> new IllegalArgumentException("Asset with id " + orderRequestDto.assetId() + " not found."));

        Order order = OrderMapper.toEntity(orderRequestDto, user, asset);
        Order savedOrder = orderRepository.save(order);

        return OrderMapper.toDto(savedOrder);
    }
    public OrderResponseDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order with id " + orderId + " not found."));

        order.setStatus(OrderStatus.CANCELLED);
        Order updatedOrder = orderRepository.save(order);

        return OrderMapper.toDto(updatedOrder);
    }
    public OrderResponseDto updateOrder(Long orderId, OrderRequestDto orderRequestDto) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new IllegalArgumentException("Order with id " + orderId + " not found."));
//
//        // Update the order details
//        order.setPrice(orderRequestDto.price());
//        order.setQuantity(orderRequestDto.quantity());
//        order.setStatus(orderRequestDto.status());
//        order.setType(orderRequestDto.type());
//
//        Order updatedOrder = orderRepository.save(order);
//
//        return OrderMapper.toDto(updatedOrder);
        return null;
    }
    public OrderResponseDto getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order with id " + orderId + " not found."));

        return OrderMapper.toDto(order);

    }
    public List<OrderResponseDto> getAllOrdersForUser(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found."));
//
//        List<Order> orders = orderRepository.findAllByUser(user);
//
//        return orders.stream()
//                .map(OrderMapper::toDto)
//                .toList();
        return null;

    }


}
