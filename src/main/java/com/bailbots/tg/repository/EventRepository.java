package com.bailbots.tg.repository;

import com.bailbots.tg.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByOrderByDateAsc();
}
