package org.dev.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.dev.server.dto.order.OrderRequestDto;
import org.dev.server.dto.order.OrderResponseDto;
import org.dev.server.dto.order.PythonOrderRequestoDto;
import org.dev.server.mapper.OrderMapper;
import org.dev.server.model.Asset;
import org.dev.server.model.Order;
import org.dev.server.model.User;
import org.dev.server.model.enums.OrderStatus;
import org.dev.server.repository.AssetRepository;
import org.dev.server.repository.OrderRepository;
import org.dev.server.repository.UserRepository;
import org.dev.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final RestTemplate restTemplate;

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        User user = userRepository.findById(orderRequestDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User with id " + orderRequestDto.userId() + " not found."));

        Asset asset = assetRepository.findById(orderRequestDto.assetId())
                .orElseThrow(() -> new IllegalArgumentException("Asset with id " + orderRequestDto.assetId() + " not found."));

        Order order = OrderMapper.toEntity(orderRequestDto, user, asset);
        Order savedOrder = orderRepository.save(order);

        sendOrderToPythonAPI(savedOrder);

        return OrderMapper.toDto(savedOrder);
    }

    private void sendOrderToPythonAPI(Order order) {
        PythonOrderRequestoDto dto = OrderMapper.toPythonRequestDto(order);

        String url = "http://python_api:8000/order";
        System.out.println("Sending to Python: " + dto);

        try{
            restTemplate.postForObject(url, dto, String.class);
        } catch (Exception e) {
            System.err.println("Failed to send order to Python API: " + e.getMessage());
        }
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
    public List<OrderResponseDto> getAllOrdersForUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found."));

        List<Order> orders = orderRepository.findAllByUser(user);

        return orders.stream()
                .map(OrderMapper::toDto)
                .toList();

    }
    @Override
    public List<OrderResponseDto> getAllOrdersForAsset(Long assetId) {
        List<Order> orders = orderRepository.fetchByAssetJpa(assetId);
        return orders.stream().map(OrderMapper::toDto).toList();
    }


}
