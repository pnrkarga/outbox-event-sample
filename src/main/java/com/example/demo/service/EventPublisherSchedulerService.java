package com.example.demo.service;

import com.example.demo.model.entity.OutboxEvent;
import com.example.demo.model.enumtype.EventType;
import com.example.demo.model.enumtype.OrderStatusType;
import com.example.demo.model.queue.OrderCreateMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisherSchedulerService {

    private final EventPublisherService eventPublisherService;
    private final ObjectMapper objectMapper;
    private final AtomicLong orderIdCounter = new AtomicLong(1);

    @Scheduled(fixedRateString = "${event.scheduler.publisherRate}")
    public void scheduleEvents() {
        try {
            long orderId = orderIdCounter.getAndIncrement(); // Generate next ID
            OrderCreateMessage message = new OrderCreateMessage(orderId, OrderStatusType.CREATED);
            byte[] payload = objectMapper.writeValueAsBytes(message);

            OutboxEvent event = new OutboxEvent();
            event.setEventType(EventType.ORDER_CREATED);
            event.setPayload(payload);

            eventPublisherService.publishEvent(event);
            log.info("Published OrderCreated event for orderId: {}", orderId);
        } catch (Exception e) {
            log.error("Failed to publish OrderCreated event", e);
        }
    }
}