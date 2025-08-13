package com.vn.backend.controllers;

import com.vn.backend.dto.response.OrderResponseDTO;
import com.vn.backend.dto.response.ItemResponseDTO;
import com.vn.backend.services.OrderService;
import com.vn.backend.dto.response.ResponseData;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/api/orders/create")
    public ResponseData<OrderResponseDTO> createOrder(HttpServletRequest request) {
        OrderResponseDTO order = orderService.createOrder(request);
        return ResponseData.success(order);
    }
}
