package com.example.demo.model.enumtype;

public enum OrderStatusType {
    CREATED(0),
    UPDATED(1),
    CANCELLED(2);

    private final Integer value;

    public Integer getValue() {
        return this.value;
    }

    private OrderStatusType(Integer value) {
        this.value = value;
    }
}
