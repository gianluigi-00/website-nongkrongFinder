package com.nongkrongfinder.repository;

import com.nongkrongfinder.entity.Event;
import com.nongkrongfinder.entity.PesertaEvent;
import com.nongkrongfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PesertaEventRepository extends JpaRepository<PesertaEvent, Long> {
    List<PesertaEvent> findByUser(User user);
    List<PesertaEvent> findByEvent(Event event);
    List<PesertaEvent> findByUserAndEvent(User user, Event event);
    boolean existsByUserAndEvent(User user, Event event);
    long countByEvent(Event event);
    void deleteByEvent(Event event);
    void deleteByUser(User user);
}
