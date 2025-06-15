package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventSchedulerService {

    private final MessagePollingService messagePollingService;

    @Scheduled(fixedRateString = "${event.scheduler.fixedRate}")
    public void scheduleEvents() {
        messagePollingService.pollOutboxEvents();
    }
}
