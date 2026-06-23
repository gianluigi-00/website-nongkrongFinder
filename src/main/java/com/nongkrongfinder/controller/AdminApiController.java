package com.nongkrongfinder.controller;

import com.nongkrongfinder.dto.*;
import com.nongkrongfinder.entity.User;
import com.nongkrongfinder.repository.UserRepository;
import com.nongkrongfinder.service.*;
import com.nongkrongfinder.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final TempatService tempatService;
    private final SearchService searchService;
    private final ReviewService reviewService;
    private final EventService eventService;

    public AdminApiController(UserService userService,
                              UserRepository userRepository,
                              TempatService tempatService,
                              SearchService searchService,
                              ReviewService reviewService,
                              EventService eventService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.tempatService = tempatService;
        this.searchService = searchService;
        this.reviewService = reviewService;
        this.eventService = eventService;
    }

    @GetMapping("/users")
    public List<UserDto> users(HttpSession session) {
        SecurityUtil.requireAdmin(session, userRepository);
        return userService.findAll().stream().map(UserDto::from).toList();
    }

    @PostMapping("/users")
    public UserDto createUser(@RequestBody UserRequest request, HttpSession session) {
        SecurityUtil.requireAdmin(session, userRepository);
        return UserDto.from(userService.createByAdmin(request));
    }

    @PutMapping("/users/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserRequest request, HttpSession session) {
        SecurityUtil.requireAdmin(session, userRepository);
        return UserDto.from(userService.updateByAdmin(id, request));
    }

    @DeleteMapping("/users/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id, HttpSession session) {
        User admin = SecurityUtil.requireAdmin(session, userRepository);
        if (admin.getIdUser().equals(id)) {
            throw new com.nongkrongfinder.exception.BadRequestException("Admin tidak bisa menghapus akun sendiri saat sedang login.");
        }
        userService.deleteUser(id);
        return Map.of("success", true, "message", "User berhasil dihapus.");
    }

    @GetMapping("/tempat")
    public List<TempatDto> tempat(HttpSession session) {
        User admin = SecurityUtil.requireAdmin(session, userRepository);
        return searchService.cariDanFilterTempat(null, null, null, null, null, null, "nama_asc")
                .stream()
                .map(t -> tempatService.toDto(t, admin))
                .toList();
    }

    @GetMapping("/review")
    public List<ReviewDto> review(HttpSession session) {
        SecurityUtil.requireAdmin(session, userRepository);
        return reviewService.findAll().stream()
                .map(r -> ReviewDto.from(r, SecurityUtil.currentUserId(session)))
                .toList();
    }

    @GetMapping("/event")
    public List<EventDto> event(HttpSession session) {
        User admin = SecurityUtil.requireAdmin(session, userRepository);
        return eventService.findAll().stream()
                .map(e -> eventService.toDto(e, admin))
                .toList();
    }
}
