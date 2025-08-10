package com.vn.backend.services;

import com.vn.backend.dto.response.OrderResponseDTO;
import com.vn.backend.dto.response.ItemResponseDTO;

import com.vn.backend.entities.Order;
import com.vn.backend.repositories.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                    oi.getUnitPrice()
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

    private String mapStatus(Integer status) {
        switch (status) {
            case 0: return "pending";
            case 1: return "confirmed";
            case 2: return "cancelled";
            default: return "completed";
        }
    }
}
