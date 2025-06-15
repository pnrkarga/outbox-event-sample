package com.example.demo.service;

import com.example.demo.model.entity.OutboxEvent;
import com.example.demo.model.enumtype.EventStatusType;
import com.example.demo.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagePollingService {

    private final OutboxEventRepository outboxEventRepository;
    private final EventPublisherService eventPublisherService;
    private final OutboxEventHistoryService outboxEventHistoryService;
    private static final int LIMIT = 10;

    public void pollOutboxEvents() {
        List<OutboxEvent> events = outboxEventRepository.findByEventStatusTypeWithLimit(LIMIT);
        List<Long> eventIds = events.stream().map(OutboxEvent::getId).toList();
        if (CollectionUtils.isNotEmpty(eventIds)) {
            log.info("Events is locked: {}, size: {}", eventIds, events.size());
            outboxEventRepository.updateStatusAsBatch(eventIds, EventStatusType.INIT, EventStatusType.PROCESSING);
        }
        for (OutboxEvent event : events) {
            try {
                eventPublisherService.publishEvent(event);
                outboxEventHistoryService.deleteEventAndSaveHistory(event);
            } catch (Exception e) {
                log.error("Failed to publish event: {}", event.getId());
                event.setEventStatusType(EventStatusType.FAILED);
                outboxEventRepository.save(event);
            }
        }
    }
}