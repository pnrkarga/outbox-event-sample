package com.example.demo.model.queue;

import com.example.demo.model.enumtype.OrderStatusType;

import java.io.Serializable;

public record OrderCreateMessage(Long orderId, OrderStatusType orderStatusType) implements Serializable {
}
