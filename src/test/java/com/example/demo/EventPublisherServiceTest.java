package com.example.demo;

import com.example.demo.configuration.RabbitMQConfig;
import com.example.demo.model.entity.OutboxEvent;
import com.example.demo.model.enumtype.EventType;
import com.example.demo.model.enumtype.OrderStatusType;
import com.example.demo.model.queue.OrderCreateMessage;
import com.example.demo.service.EventPublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class EventPublisherServiceTest {

    @Container
    public static final RabbitMQContainer rabbitMQContainer =
            new RabbitMQContainer("rabbitmq:3-management")
                    .withExposedPorts(5672, 15672);

    @Autowired
    private EventPublisherService eventPublisherService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DynamicPropertySource
    static void rabbitProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", () -> rabbitMQContainer.getMappedPort(5672));
        registry.add("spring.rabbitmq.username", () -> "guest");
        registry.add("spring.rabbitmq.password", () -> "guest");
    }

    @BeforeEach
    void setup() {
        rabbitTemplate = spy(rabbitTemplate);
        eventPublisherService = new EventPublisherService(rabbitTemplate);
    }

    // Generate multiple OrderCreateMessage test cases
    static Stream<OrderCreateMessage> orderCreateMessageProvider() {
        return Stream.of(
                new OrderCreateMessage(1L, OrderStatusType.CREATED),
                new OrderCreateMessage(2L, OrderStatusType.CREATED),
                new OrderCreateMessage(3L, OrderStatusType.CREATED),
                new OrderCreateMessage(4L, OrderStatusType.CREATED)
        );
    }

    @ParameterizedTest
    @MethodSource("orderCreateMessageProvider")
    void publish_event_successfully(OrderCreateMessage message) throws Exception {
        //Given
        OutboxEvent event = new OutboxEvent();
        event.setId(1L);
        event.setEventType(EventType.ORDER_CREATED);
        byte[] payload = objectMapper.writeValueAsBytes(message);
        event.setPayload(payload);

        //When
        eventPublisherService.publishEvent(event);

        //Then
        verify(rabbitTemplate, times(1))
                .convertAndSend(eq(RabbitMQConfig.EXCHANGE_NAME), eq("order.created"), eq(payload));
    }

    @Test
    void send_to_dlq_successfully() throws Exception {
        //Given
        OutboxEvent event = new OutboxEvent();
        event.setId(10L);
        event.setEventType(EventType.ORDER_CREATED);
        OrderCreateMessage message = new OrderCreateMessage(1L, OrderStatusType.CREATED);
        byte[] payload = objectMapper.writeValueAsBytes(message);
        event.setPayload(payload);

        //When
        eventPublisherService.sendToDLQ(event);

        //Then
        verify(rabbitTemplate, times(1))
                .convertAndSend(eq(RabbitMQConfig.DLQ_EXCHANGE_NAME), eq(RabbitMQConfig.DLQ_ROUTING_KEY), eq(payload));
    }
}