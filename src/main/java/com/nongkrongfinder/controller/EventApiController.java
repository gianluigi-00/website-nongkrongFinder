package com.nongkrongfinder.controller;

import com.nongkrongfinder.dto.EventDto;
import com.nongkrongfinder.dto.EventRequest;
import com.nongkrongfinder.dto.PesertaEventDto;
import com.nongkrongfinder.entity.Event;
import com.nongkrongfinder.entity.User;
import com.nongkrongfinder.repository.UserRepository;
import com.nongkrongfinder.service.EventService;
import com.nongkrongfinder.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/event")
public class EventApiController {

    private final EventService eventService;
    private final UserRepository userRepository;

    public EventApiController(EventService eventService, UserRepository userRepository) {
        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<EventDto> list(HttpSession session) {
        User current = currentUserOrNull(session);
        return eventService.findAll().stream()
                .map(event -> eventService.toDto(event, current))
                .toList();
    }

    @GetMapping("/{id}")
    public EventDto detail(@PathVariable Long id, HttpSession session) {
        return eventService.toDto(eventService.findById(id), currentUserOrNull(session));
    }

    @PostMapping
    public EventDto create(@RequestBody EventRequest request, HttpSession session) {
        User user = SecurityUtil.requireLogin(session, userRepository);
        Event event = eventService.create(request, user);
        return eventService.toDto(event, user);
    }

    @PutMapping("/{id}")
    public EventDto update(@PathVariable Long id, @RequestBody EventRequest request, HttpSession session) {
        Event existing = eventService.findById(id);
        Long ownerId = existing.getUser() == null ? null : existing.getUser().getIdUser();
        SecurityUtil.requireOwnerOrAdmin(ownerId, session);
        Event event = eventService.update(id, request);
        return eventService.toDto(event, currentUserOrNull(session));
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id, HttpSession session) {
        Event existing = eventService.findById(id);
        Long ownerId = existing.getUser() == null ? null : existing.getUser().getIdUser();
        SecurityUtil.requireOwnerOrAdmin(ownerId, session);
        eventService.delete(id);
        return Map.of("success", true, "message", "Event berhasil dihapus.");
    }

    @PostMapping("/{id}/join")
    public EventDto join(@PathVariable Long id, HttpSession session) {
        User user = SecurityUtil.requireLogin(session, userRepository);
        eventService.join(id, user);
        return eventService.toDto(eventService.findById(id), user);
    }

    @DeleteMapping("/{id}/join")
    public EventDto leave(@PathVariable Long id, HttpSession session) {
        User user = SecurityUtil.requireLogin(session, userRepository);
        eventService.leave(id, user);
        return eventService.toDto(eventService.findById(id), user);
    }

    @GetMapping("/{id}/peserta")
    public List<PesertaEventDto> peserta(@PathVariable Long id) {
        return eventService.peserta(id).stream()
                .map(PesertaEventDto::from)
                .toList();
    }

    private User currentUserOrNull(HttpSession session) {
        Long id = SecurityUtil.currentUserId(session);
        if (id == null) {
            return null;
        }
        return userRepository.findById(id).orElse(null);
    }
}
