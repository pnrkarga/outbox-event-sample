package com.example.demo.service;

import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.OutboxEvent;
import com.example.demo.model.enumtype.EventStatusType;
import com.example.demo.model.enumtype.EventType;
import com.example.demo.model.enumtype.OrderStatusType;
import com.example.demo.model.queue.OrderCreateMessage;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setCreatedAt(Date.from(LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        outboxEvent.setUniqueReferenceId(UUID.randomUUID().toString().replace("-", ""));
        outboxEvent.setEventType(EventType.ORDER_CREATED);
        outboxEvent.setEventStatusType(EventStatusType.INIT);
        try {
            OrderCreateMessage message = new OrderCreateMessage(savedOrder.getId(), OrderStatusType.CREATED);
            String payload = objectMapper.writeValueAsString(message);
            outboxEvent.setPayload(payload.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize OrderCreateMessage", e);
        }
        outboxEventRepository.save(outboxEvent);
        return savedOrder;
    }
}
