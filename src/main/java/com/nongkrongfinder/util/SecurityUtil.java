package com.nongkrongfinder.util;

import com.nongkrongfinder.entity.User;
import com.nongkrongfinder.exception.ForbiddenException;
import com.nongkrongfinder.exception.UnauthorizedException;
import com.nongkrongfinder.repository.UserRepository;
import jakarta.servlet.http.HttpSession;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static Long currentUserId(HttpSession session) {
        Object id = session.getAttribute("userId");
        if (id instanceof Long longId) {
            return longId;
        }
        if (id instanceof Integer intId) {
            return intId.longValue();
        }
        return null;
    }

    public static String currentRole(HttpSession session) {
        Object role = session.getAttribute("role");
        return role == null ? null : role.toString();
    }

    public static boolean isLoggedIn(HttpSession session) {
        return currentUserId(session) != null;
    }

    public static boolean isAdmin(HttpSession session) {
        return "ADMIN".equalsIgnoreCase(currentRole(session));
    }

    public static User requireLogin(HttpSession session, UserRepository userRepository) {
        Long id = currentUserId(session);
        if (id == null) {
            throw new UnauthorizedException("Silakan login terlebih dahulu.");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("Session tidak valid. Silakan login ulang."));
    }

    public static User requireAdmin(HttpSession session, UserRepository userRepository) {
        User user = requireLogin(session, userRepository);
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new ForbiddenException("Akses hanya untuk admin.");
        }
        return user;
    }

    public static void requireOwnerOrAdmin(Long ownerId, HttpSession session) {
        Long currentId = currentUserId(session);
        if (currentId == null) {
            throw new UnauthorizedException("Silakan login terlebih dahulu.");
        }
        if (!isAdmin(session) && (ownerId == null || !currentId.equals(ownerId))) {
            throw new ForbiddenException("Kamu tidak punya akses untuk mengubah data ini.");
        }
    }
}
