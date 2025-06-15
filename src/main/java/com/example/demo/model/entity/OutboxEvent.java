package com.example.demo.model.entity;

import com.example.demo.model.enumtype.EventStatusType;
import com.example.demo.model.enumtype.EventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "outbox_event")
@Getter
@Setter
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Lob
    @Column(name = "payload", columnDefinition = "bytea", nullable = false)
    private byte[] payload;

    @Column(name = "event_status")
    private EventStatusType eventStatusType;

    @Column(name = "event_type")
    private EventType eventType;

    @Column(name = "unique_reference_id")
    private String uniqueReferenceId;
}
