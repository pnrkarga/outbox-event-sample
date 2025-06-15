package com.example.demo.service;

import com.example.demo.model.entity.OutboxEvent;
import com.example.demo.model.entity.OutboxEventHistory;
import com.example.demo.model.enumtype.EventStatusType;
import com.example.demo.repository.OutboxEventHistoryRepository;
import com.example.demo.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventHistoryService {

    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventHistoryRepository outboxEventHistoryRepository;

    public void deleteEventAndSaveHistory(OutboxEvent event) {
        log.info("Deleting event: {}", event.getId());
        saveEventHistory(event, EventStatusType.SENT);

        outboxEventRepository.delete(event);
    }
    private void saveEventHistory(OutboxEvent event, EventStatusType status) {
        OutboxEventHistory eventHistory = new OutboxEventHistory();
        eventHistory.setEventStatusType(status);
        eventHistory.setOutboxEventId(event.getId());
        eventHistory.setOutboxEventCreatedAt(event.getCreatedAt());
        eventHistory.setPayload(event.getPayload());
        eventHistory.setOutboxEventUpdatedAt(event.getUpdatedAt());
        outboxEventHistoryRepository.save(eventHistory);
    }
}
