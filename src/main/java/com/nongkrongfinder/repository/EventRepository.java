package com.nongkrongfinder.repository;

import com.nongkrongfinder.entity.Event;
import com.nongkrongfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUser(User user);
}
