package com.vn.backend.controllers;

import com.vn.backend.dto.request.OrderUpdateRequest;
import com.vn.backend.dto.response.OrderResponseDTO;
import com.vn.backend.services.OrderService;

import org.springframework.web.bind.annotation.RequestBody;

import com.vn.backend.dto.response.ResponseData;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/api/orders")
    public ResponseData<List<OrderResponseDTO>> getOrders(HttpServletRequest request) {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseData.success(orders);
    }

    @PutMapping("/api/orders/{id}")
    public OrderResponseDTO updateStatus(
        @PathVariable Long id,
        @RequestBody OrderUpdateRequest request
    ) {
        return orderService.updateOrderStatus(id, request.getStatus());
    }
}
