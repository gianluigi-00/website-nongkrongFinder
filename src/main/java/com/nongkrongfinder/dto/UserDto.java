package com.nongkrongfinder.dto;

import com.nongkrongfinder.entity.User;
import java.time.LocalDateTime;

public record UserDto(
        Long idUser,
        String nama,
        String email,
        String role,
        String fotoProfil,
        LocalDateTime createdAt
) {
    public static UserDto from(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getIdUser(),
                user.getNama(),
                user.getEmail(),
                user.getRole(),
                user.getFotoProfil(),
                user.getCreatedAt()
        );
    }
}
