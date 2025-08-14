package com.vn.backend.services;

import com.vn.backend.dto.request.CreateOrderRequest;
import com.vn.backend.dto.response.CreateOrderResponse;
import com.vn.backend.dto.response.OrderResponseDTO;
import com.vn.backend.dto.response.ItemResponseDTO;

import com.vn.backend.entities.Order;
import com.vn.backend.entities.OrderItem;
import com.vn.backend.entities.User;
import com.vn.backend.entities.Product;
import com.vn.backend.exceptions.InvalidDataException;
import com.vn.backend.exceptions.NotFoundException;
import com.vn.backend.repositories.OrderRepository;

import com.vn.backend.repositories.ProductRepository;
import com.vn.backend.utils.enums.OrderStatusType;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private AuthService authService;
    @Autowired
    private ProductRepository productRepository;

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
                oi.getUnitPrice()
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
    @Transactional
    public CreateOrderResponse createOrder(List<CreateOrderRequest> createOrderRequest){
        User user = authService.getCurrentUser();
        Order order = new Order();
        order.setUser(user);
        order.setAddress(user.getAddress());
        List<OrderItem> orderItems = new ArrayList<>();
        for(CreateOrderRequest createOrderRequest1: createOrderRequest){
            Product product = productRepository.findById(createOrderRequest1.getProductId()).orElseThrow(() -> new NotFoundException("Sản phẩm id " + createOrderRequest1.getProductId() + " không tồn tại"));
            if(product.getStockQuantity() < createOrderRequest1.getQuantity()){
                throw new InvalidDataException("Sản phẩm " + product.getName() + " không đủ số lượng");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setUnitPrice(product.getPrice().multiply(BigDecimal.valueOf(createOrderRequest1.getQuantity())));
            product.setStockQuantity(product.getStockQuantity() - createOrderRequest1.getQuantity());
            orderItem.setQuantity(createOrderRequest1.getQuantity());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        order.setTotalAmount(orderItems.stream().map(OrderItem::getUnitPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setStatus(OrderStatusType.PENDING.getValue());
        Long orderId=orderRepository.save(order).getId();
        // Cập nhật Stock Quantity
        for(OrderItem orderItem : orderItems){
            Product product = orderItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() - orderItem.getQuantity());
            productRepository.save(product);
        }
        return new CreateOrderResponse(orderId, order.getTotalAmount(), orderItems.stream().map(orderItem -> new CreateOrderResponse.ProductOrderResponse(orderItem.getProduct().getId(), orderItem.getProduct().getName(), orderItem.getProduct().getThumbnailUrl())).collect(Collectors.toList()));

    }
}
