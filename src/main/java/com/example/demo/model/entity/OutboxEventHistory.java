package com.example.demo.model.entity;

import com.example.demo.model.enumtype.EventStatusType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Date;

@Entity
@Table(name = "outbox_event")
@Getter
@Setter
public class OutboxEventHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "outbox_event_id")
    private Long outboxEventId;

    @Column(name = "outbox_event_created_at")
    private Date outboxEventCreatedAt;

    @Column(name = "outbox_event_updated_at")
    protected Date outboxEventUpdatedAt;

    @Lob
    @Column(name = "payload", columnDefinition = "bytea", nullable = false)
    private byte[] payload;

    @Column(name = "event_status")
    private EventStatusType eventStatusType;

    @Column(name = "unique_reference_id")
    private String uniqueReferenceId;
}
