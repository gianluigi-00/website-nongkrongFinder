package com.nongkrongfinder.dto;

import com.nongkrongfinder.entity.Review;
import java.time.LocalDateTime;

public record ReviewDto(
        Long idReview,
        Long idUser,
        String namaUser,
        Long idTempat,
        String namaTempat,
        Integer rating,
        String komentar,
        LocalDateTime tanggalReview,
        boolean owner
) {
    public static ReviewDto from(Review review, Long currentUserId) {
        Long idUser = review.getUser() == null ? null : review.getUser().getIdUser();
        String namaUser = review.getUser() == null ? "-" : review.getUser().getNama();
        Long idTempat = review.getTempat() == null ? null : review.getTempat().getIdTempat();
        String namaTempat = review.getTempat() == null ? "-" : review.getTempat().getNamaTempat();
        return new ReviewDto(
                review.getIdReview(),
                idUser,
                namaUser,
                idTempat,
                namaTempat,
                review.getRating(),
                review.getKomentar(),
                review.getTanggalReview(),
                currentUserId != null && currentUserId.equals(idUser)
        );
    }
}
