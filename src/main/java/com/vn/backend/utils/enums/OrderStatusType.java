package com.vn.backend.utils.enums;

public enum OrderStatusType {
    PENDING(0,"Chờ xử lý"),        // Chờ xử lý
    CONFIRMED(1,"Đã xác nhận"),      // Đã xác nhận
    SHIPPING(2,"Đang giao hàng"),       // Đang giao hàng
    DELIVERED(3,"Đã giao hàng"),      // Đã giao hàng
    CANCELLED(4,"Đã hủy");      // Đã hủy

    private final int value;
    private final String description;

    OrderStatusType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }
    public String getDescription() {return description;}
}