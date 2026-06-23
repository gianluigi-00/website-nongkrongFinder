package com.nongkrongfinder.service;

import com.nongkrongfinder.dto.ProfileRequest;
import com.nongkrongfinder.dto.RegisterRequest;
import com.nongkrongfinder.dto.UserRequest;
import com.nongkrongfinder.entity.Event;
import com.nongkrongfinder.entity.Tempat;
import com.nongkrongfinder.entity.User;
import com.nongkrongfinder.exception.BadRequestException;
import com.nongkrongfinder.exception.NotFoundException;
import com.nongkrongfinder.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TempatRepository tempatRepository;
    private final ReviewRepository reviewRepository;
    private final FavoritRepository favoritRepository;
    private final EventRepository eventRepository;
    private final PesertaEventRepository pesertaEventRepository;

    public UserService(UserRepository userRepository,
                       TempatRepository tempatRepository,
                       ReviewRepository reviewRepository,
                       FavoritRepository favoritRepository,
                       EventRepository eventRepository,
                       PesertaEventRepository pesertaEventRepository) {
        this.userRepository = userRepository;
        this.tempatRepository = tempatRepository;
        this.reviewRepository = reviewRepository;
        this.favoritRepository = favoritRepository;
        this.eventRepository = eventRepository;
        this.pesertaEventRepository = pesertaEventRepository;
    }

    @Transactional
    public User register(RegisterRequest request) {
        validateRequired(request.nama(), "Nama wajib diisi.");
        validateRequired(request.email(), "Email wajib diisi.");
        validateRequired(request.password(), "Password wajib diisi.");
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email sudah digunakan.");
        }
        User user = new User();
        user.setNama(request.nama().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPassword(request.password());
        user.setRole("USER");
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        validateRequired(email, "Email wajib diisi.");
        validateRequired(password, "Password wajib diisi.");
        return userRepository.findByEmail(email.trim().toLowerCase())
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(() -> new BadRequestException("Email atau password salah."));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User tidak ditemukan."));
    }

    @Transactional
    public User updateProfile(User user, ProfileRequest request) {
        validateRequired(request.nama(), "Nama wajib diisi.");
        validateRequired(request.email(), "Email wajib diisi.");
        userRepository.findByEmail(request.email().trim().toLowerCase())
                .filter(existing -> !existing.getIdUser().equals(user.getIdUser()))
                .ifPresent(existing -> {
                    throw new BadRequestException("Email sudah digunakan user lain.");
                });

        user.setNama(request.nama().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setFotoProfil(request.fotoProfil());
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(request.password());
        }
        return userRepository.save(user);
    }

    @Transactional
    public User createByAdmin(UserRequest request) {
        validateRequired(request.nama(), "Nama wajib diisi.");
        validateRequired(request.email(), "Email wajib diisi.");
        validateRequired(request.password(), "Password wajib diisi.");
        if (userRepository.existsByEmail(request.email().trim().toLowerCase())) {
            throw new BadRequestException("Email sudah digunakan.");
        }
        User user = new User();
        user.setNama(request.nama().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPassword(request.password());
        user.setRole(normalizeRole(request.role()));
        user.setFotoProfil(request.fotoProfil());
        return userRepository.save(user);
    }

    @Transactional
    public User updateByAdmin(Long id, UserRequest request) {
        User user = findById(id);
        validateRequired(request.nama(), "Nama wajib diisi.");
        validateRequired(request.email(), "Email wajib diisi.");
        userRepository.findByEmail(request.email().trim().toLowerCase())
                .filter(existing -> !existing.getIdUser().equals(id))
                .ifPresent(existing -> {
                    throw new BadRequestException("Email sudah digunakan user lain.");
                });

        user.setNama(request.nama().trim());
        user.setEmail(request.email().trim().toLowerCase());
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(request.password());
        }
        user.setRole(normalizeRole(request.role()));
        user.setFotoProfil(request.fotoProfil());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findById(id);

        // Hapus data dependensi agar tidak error foreign key.
        reviewRepository.deleteByUser(user);
        favoritRepository.deleteByUser(user);
        pesertaEventRepository.deleteByUser(user);

        List<Tempat> tempatList = tempatRepository.findByUser(user);
        for (Tempat tempat : tempatList) {
            reviewRepository.deleteByTempat(tempat);
            favoritRepository.deleteByTempat(tempat);
            tempatRepository.delete(tempat);
        }

        List<Event> eventList = eventRepository.findByUser(user);
        for (Event event : eventList) {
            pesertaEventRepository.deleteByEvent(event);
            eventRepository.delete(event);
        }

        userRepository.delete(user);
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "USER";
        }
        String normalized = role.trim().toUpperCase();
        if (!normalized.equals("USER") && !normalized.equals("ADMIN")) {
            throw new BadRequestException("Role hanya boleh USER atau ADMIN.");
        }
        return normalized;
    }

    private void validateRequired(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException(message);
        }
    }
}
