package com.example.demo.model.enumtype;

public enum EventType {
    ORDER_CREATED("ORDER_CREATED"),
    ORDER_UPDATED("ORDER_UPDATED"),
    ORDER_CANCELLED("ORDER_CANCELLED");

    private final String value;

    public String getValue() {
        return this.value;
    }

    EventType(String value) {
        this.value = value;
    }
}
