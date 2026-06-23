package com.nongkrongfinder.dto;

public record TempatRequest(
        String namaTempat,
        String alamat,
        String deskripsi,
        String kategori,
        String jamBuka,
        String jamTutup,
        String foto,
        String fasilitas,
        Integer hargaMin,
        Integer hargaMax
) {
}
