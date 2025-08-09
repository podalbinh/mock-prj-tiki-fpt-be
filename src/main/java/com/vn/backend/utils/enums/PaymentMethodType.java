package com.vn.backend.utils.enums;

public enum PaymentMethodType {
    CASH_ON_DELIVERY(1, "Thanh toán khi nhận hàng"),
    VNPAY(2, "Thanh toán qua VNPAY");

    private final int id;
    private final String description;

    PaymentMethodType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}