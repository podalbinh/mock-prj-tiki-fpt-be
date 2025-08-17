package com.vn.backend.utils.enums;

public enum OrderStatusType {
    CONFIRMED(0,"Đã xác nhận"),      // Đã xác nhận
    SHIPPING(1,"Đang giao hàng"),       // Đang giao hàng
    DELIVERED(2,"Đã giao hàng"),      // Đã giao hàng
    CANCELLED(3,"Đã hủy");      // Đã hủy

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