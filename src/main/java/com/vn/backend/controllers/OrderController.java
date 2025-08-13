package com.vn.backend.controllers;

import com.vn.backend.dto.request.CreateOrderRequest;
import com.vn.backend.dto.request.OrderUpdateRequest;
import com.vn.backend.dto.response.CreateOrderResponse;
import com.vn.backend.dto.response.OrderResponseDTO;
import com.vn.backend.services.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.vn.backend.dto.response.ResponseData;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
public class OrderController {
    Logger logger = LoggerFactory.getLogger(OrderController.class);

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

    @PostMapping("/api/orders/create")
    public ResponseData<CreateOrderResponse> createOrder(@RequestBody  List<CreateOrderRequest> request) {
        logger.info("[IN] POST /api/orders/create");
        var response =  ResponseData.created(orderService.createOrder(request));
        logger.info("[IN] POST /api/orders/create - success");
        return response;
    }
}
