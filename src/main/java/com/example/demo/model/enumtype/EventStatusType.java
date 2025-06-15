package com.example.demo.model.enumtype;

public enum EventStatusType {
    INIT(0),
    SENT(1),
    FAILED(2),
    PROCESSING(3);

    private final Integer value;

    public Integer getValue() {
        return this.value;
    }

    private EventStatusType(Integer value) {
        this.value = value;
    }
}
