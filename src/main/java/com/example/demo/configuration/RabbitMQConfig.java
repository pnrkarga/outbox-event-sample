package com.example.demo.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "order.exchange";
    public static final String ORDER_QUEUE_NAME = "order.queue";
    public static final String DLQ_EXCHANGE_NAME = "order.dlq.exchange";
    public static final String DLQ_QUEUE_NAME = "order.dlq";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";
    public static final String DLQ_ROUTING_KEY = "order.dlq";

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE_NAME)  // DLX Exchange where failed messages go
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)  // The routing key for the DLQ
                .build();
    }

    @Bean
    public Binding orderQueueBinding() {
        return BindingBuilder.bind(orderQueue())
                .to(directExchange())
                .with(ORDER_CREATED_ROUTING_KEY);  // Event routing key for events like "created"
    }

    @Bean
    public DirectExchange dlqExchange() {
        return new DirectExchange(DLQ_EXCHANGE_NAME);
    }

    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable(DLQ_QUEUE_NAME)
                .build();

    }
}
