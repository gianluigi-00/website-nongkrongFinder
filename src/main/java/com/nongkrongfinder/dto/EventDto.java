package com.nongkrongfinder.dto;

import com.nongkrongfinder.entity.Event;
import java.time.LocalDateTime;

public record EventDto(
        Long idEvent,
        Long idUser,
        String namaUser,
        String namaEvent,
        String deskripsi,
        LocalDateTime tanggalEvent,
        String lokasi,
        Integer maksPeserta,
        String status,
        LocalDateTime createdAt,
        long jumlahPeserta,
        boolean joined,
        boolean owner
) {
    public static EventDto from(Event event, long jumlahPeserta, boolean joined, Long currentUserId) {
        Long idUser = event.getUser() == null ? null : event.getUser().getIdUser();
        String namaUser = event.getUser() == null ? "-" : event.getUser().getNama();
        return new EventDto(
                event.getIdEvent(),
                idUser,
                namaUser,
                event.getNamaEvent(),
                event.getDeskripsi(),
                event.getTanggalEvent(),
                event.getLokasi(),
                event.getMaksPeserta(),
                event.getStatus(),
                event.getCreatedAt(),
                jumlahPeserta,
                joined,
                currentUserId != null && currentUserId.equals(idUser)
        );
    }
}
