package com.example.demo.service;

import com.example.demo.model.entity.OutboxEvent;
import com.example.demo.model.enumtype.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import static com.example.demo.configuration.RabbitMQConfig.DLQ_EXCHANGE_NAME;
import static com.example.demo.configuration.RabbitMQConfig.DLQ_ROUTING_KEY;

@Service
@Slf4j
public class EventPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public EventPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Retryable(retryFor = { Exception.class }, backoff = @Backoff(delay = 2000))
    public void publishEvent(OutboxEvent event) {
        try {
            String routingKey = determineRoutingKey(event.getEventType());
            rabbitTemplate.convertAndSend("order.exchange", routingKey, event.getPayload());
            log.info("Event Published: {}", event.getId());
        } catch (Exception e) {
            log.error("Error publishing event: {}", event.getId() + ". Error: " + e.getMessage());
            throw e;
        }
    }

    private String determineRoutingKey(EventType eventType) {
        return switch (eventType) {
            case ORDER_CREATED -> "order.created";
            case ORDER_UPDATED -> "order.updated";
            case ORDER_CANCELLED -> "order.cancelled";
        };
    }

    public void sendToDLQ(OutboxEvent event) {
        rabbitTemplate.convertAndSend(DLQ_EXCHANGE_NAME, DLQ_ROUTING_KEY, event.getPayload());
        log.info("Event sent to DLQ: {}", event.getId());
    }
}

