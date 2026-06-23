package com.nongkrongfinder.controller;

import com.nongkrongfinder.dto.*;
import com.nongkrongfinder.entity.User;
import com.nongkrongfinder.repository.UserRepository;
import com.nongkrongfinder.service.UserService;
import com.nongkrongfinder.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthApiController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody RegisterRequest request) {
        return UserDto.from(userService.register(request));
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody LoginRequest request, HttpSession session) {
        User user = userService.login(request.email(), request.password());
        session.setAttribute("userId", user.getIdUser());
        session.setAttribute("role", user.getRole());
        return UserDto.from(user);
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {
        session.invalidate();
        return Map.of("success", true, "message", "Logout berhasil.");
    }

    @GetMapping("/me")
    public UserDto me(HttpSession session) {
        User user = SecurityUtil.requireLogin(session, userRepository);
        return UserDto.from(user);
    }

    @PutMapping("/profile")
    public UserDto updateProfile(@RequestBody ProfileRequest request, HttpSession session) {
        User user = SecurityUtil.requireLogin(session, userRepository);
        User updated = userService.updateProfile(user, request);
        session.setAttribute("role", updated.getRole());
        return UserDto.from(updated);
    }
}
