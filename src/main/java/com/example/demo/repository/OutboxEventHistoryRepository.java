package com.example.demo.repository;

import com.example.demo.model.entity.OutboxEvent;
import com.example.demo.model.entity.OutboxEventHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxEventHistoryRepository extends JpaRepository<OutboxEventHistory, Long> {

}
