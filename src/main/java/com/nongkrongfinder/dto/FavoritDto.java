package com.nongkrongfinder.dto;

import com.nongkrongfinder.entity.Favorit;
import java.time.LocalDateTime;

public record FavoritDto(
        Long idFavorit,
        LocalDateTime tanggalFavorit,
        TempatDto tempat
) {
    public static FavoritDto from(Favorit favorit, TempatDto tempatDto) {
        return new FavoritDto(favorit.getIdFavorit(), favorit.getTanggalFavorit(), tempatDto);
    }
}
