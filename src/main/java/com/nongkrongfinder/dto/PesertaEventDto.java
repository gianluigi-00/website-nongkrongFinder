package com.nongkrongfinder.dto;

import com.nongkrongfinder.entity.PesertaEvent;
import java.time.LocalDateTime;

public record PesertaEventDto(
        Long idPeserta,
        Long idUser,
        String namaUser,
        String emailUser,
        Long idEvent,
        String namaEvent,
        LocalDateTime tanggalJoin
) {
    public static PesertaEventDto from(PesertaEvent peserta) {
        return new PesertaEventDto(
                peserta.getIdPeserta(),
                peserta.getUser().getIdUser(),
                peserta.getUser().getNama(),
                peserta.getUser().getEmail(),
                peserta.getEvent().getIdEvent(),
                peserta.getEvent().getNamaEvent(),
                peserta.getTanggalJoin()
        );
    }
}
