package com.vn.backend.services;

import com.vn.backend.dto.response.OrderResponseDTO;
import com.vn.backend.dto.response.UserOrderResponseDTO;
import com.vn.backend.dto.response.ItemResponseDTO;

import com.vn.backend.entities.Order;
import com.vn.backend.exceptions.NotFoundException;
import com.vn.backend.repositories.OrderRepository;

import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(order -> {
            List<ItemResponseDTO> items = order.getOrderItems().stream()
                .map(oi -> new ItemResponseDTO(
                    oi.getId(),
                    oi.getQuantity(),
                    oi.getProduct().getName(),
                    oi.getUnitPrice(),
                    oi.getProduct().getThumbnailUrl() 
                )).collect(Collectors.toList());

            String statusStr = mapStatus(order.getStatus());

            return new OrderResponseDTO(
                order.getId(),
                order.getUser().getFullName(), // giả sử User có getFullName()
                items,
                order.getTotalAmount(),
                statusStr
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(Long orderId, String statusStr) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng với ID: " + orderId));

        Integer status = mapStatusStringToInteger(statusStr);
        order.setStatus(status);

        Order updatedOrder = orderRepository.save(order);

        // Map lại thành DTO trả về
        List<ItemResponseDTO> items = updatedOrder.getOrderItems().stream()
            .map(oi -> new ItemResponseDTO(
                oi.getId(),
                oi.getQuantity(),
                oi.getProduct().getName(),
                oi.getUnitPrice(),
                oi.getProduct().getThumbnailUrl() 
            )).collect(Collectors.toList());

        return new OrderResponseDTO(
            updatedOrder.getId(),
            updatedOrder.getUser().getFullName(),
            items,
            updatedOrder.getTotalAmount(),
            statusStr
        );
    }

    private Integer mapStatusStringToInteger(String statusStr) {
        switch (statusStr.toLowerCase()) {
            case "pending": return 0;
            case "confirmed": return 1;
            case "cancelled": return 2;
            case "completed": return 3;
            default: throw new IllegalArgumentException("Trạng thái không hợp lệ: " + statusStr);
        }
    }

    private String mapStatus(Integer status) {
        switch (status) {
            case 0: return "pending";
            case 1: return "confirmed";
            case 2: return "cancelled";
            default: return "completed";
        }
    }

    public List<UserOrderResponseDTO> getOrdersByUser(Long userId) {
    List<Order> orders = orderRepository.findByUserId(userId);

    return orders.stream().map(order -> {
        List<ItemResponseDTO> items = order.getOrderItems().stream()
            .map(oi -> new ItemResponseDTO(
                oi.getId(),
                oi.getQuantity(),
                oi.getProduct().getName(),
                oi.getUnitPrice(),
                oi.getProduct().getThumbnailUrl() 
            ))
            .toList();

        return new UserOrderResponseDTO(
            order.getId(),
            order.getUser().getFullName(),
            order.getAddress(),
            order.getTotalAmount(),
            mapStatus(order.getStatus()),
            order.getCreatedAt(),
            order.getUpdatedAt(),
            items
        );
            }).toList();
    }

    public UserOrderResponseDTO getOrderDetailById(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng"));

        if (!order.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn chưa đăng nhập");
        }

        List<ItemResponseDTO> items = order.getOrderItems().stream()
            .map(oi -> new ItemResponseDTO(
                oi.getId(),
                oi.getQuantity(),
                oi.getProduct().getName(),
                oi.getUnitPrice(),
                oi.getProduct().getThumbnailUrl() 
            )).toList();

        return new UserOrderResponseDTO(
            order.getId(),
            order.getUser().getFullName(),
            order.getAddress(),
            order.getTotalAmount(),
            mapStatus(order.getStatus()),
            order.getCreatedAt(),
            order.getUpdatedAt(),
            items
        );
    }

}
