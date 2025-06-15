package com.example.demo.repository;

import com.example.demo.model.entity.OutboxEvent;
import com.example.demo.model.enumtype.EventStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    @Query(value = "SELECT * FROM outbox_event WHERE event_status = 0 ORDER BY created_at ASC LIMIT :limit FOR UPDATE SKIP LOCKED", nativeQuery = true)
    List<OutboxEvent> findByEventStatusTypeWithLimit(@Param("limit") int limit);

    @Modifying
    @Transactional
    @Query("update OutboxEvent oe set oe.eventStatusType = :newStatus, oe.updatedAt = CURRENT_TIMESTAMP where oe.id in (:idList) and oe.eventStatusType= :reqStatus")
    void updateStatusAsBatch(@Param("idList") List<Long> idList, @Param("reqStatus") EventStatusType reqStatus, @Param("newStatus") EventStatusType newStatus);

}
