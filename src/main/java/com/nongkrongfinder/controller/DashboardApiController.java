package com.nongkrongfinder.controller;

import com.nongkrongfinder.dto.DashboardStatsDto;
import com.nongkrongfinder.repository.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    private final UserRepository userRepository;
    private final TempatRepository tempatRepository;
    private final ReviewRepository reviewRepository;
    private final FavoritRepository favoritRepository;
    private final EventRepository eventRepository;

    public DashboardApiController(UserRepository userRepository, TempatRepository tempatRepository,
                                  ReviewRepository reviewRepository, FavoritRepository favoritRepository,
                                  EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.tempatRepository = tempatRepository;
        this.reviewRepository = reviewRepository;
        this.favoritRepository = favoritRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/stats")
    public DashboardStatsDto stats() {
        return new DashboardStatsDto(
                userRepository.count(),
                tempatRepository.count(),
                reviewRepository.count(),
                favoritRepository.count(),
                eventRepository.count()
        );
    }
}
