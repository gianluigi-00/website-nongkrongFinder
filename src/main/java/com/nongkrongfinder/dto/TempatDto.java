package com.nongkrongfinder.dto;

import com.nongkrongfinder.entity.Tempat;
import java.time.LocalDateTime;

public record TempatDto(
        Long idTempat,
        Long idUser,
        String namaUser,
        String namaTempat,
        String alamat,
        String deskripsi,
        String kategori,
        String jamBuka,
        String jamTutup,
        String foto,
        String fasilitas,
        Integer hargaMin,
        Integer hargaMax,
        LocalDateTime createdAt,
        double ratingRataRata,
        long jumlahReview,
        boolean favorit
) {
    public static TempatDto from(Tempat tempat, double ratingRataRata, long jumlahReview, boolean favorit) {
        Long idUser = tempat.getUser() == null ? null : tempat.getUser().getIdUser();
        String namaUser = tempat.getUser() == null ? "-" : tempat.getUser().getNama();
        return new TempatDto(
                tempat.getIdTempat(),
                idUser,
                namaUser,
                tempat.getNamaTempat(),
                tempat.getAlamat(),
                tempat.getDeskripsi(),
                tempat.getKategori(),
                tempat.getJamBuka(),
                tempat.getJamTutup(),
                tempat.getFoto(),
                tempat.getFasilitas(),
                tempat.getHargaMin(),
                tempat.getHargaMax(),
                tempat.getCreatedAt(),
                ratingRataRata,
                jumlahReview,
                favorit
        );
    }
}
