package com.nongkrongfinder.dto;

import java.time.LocalDateTime;

public record EventRequest(
        String namaEvent,
        String deskripsi,
        LocalDateTime tanggalEvent,
        String lokasi,
        Integer maksPeserta,
        String status
) {
}
