package com.nongkrongfinder.service;

import com.nongkrongfinder.dto.TempatDto;
import com.nongkrongfinder.dto.TempatRequest;
import com.nongkrongfinder.entity.Tempat;
import com.nongkrongfinder.entity.User;
import com.nongkrongfinder.exception.BadRequestException;
import com.nongkrongfinder.exception.NotFoundException;
import com.nongkrongfinder.repository.FavoritRepository;
import com.nongkrongfinder.repository.ReviewRepository;
import com.nongkrongfinder.repository.TempatRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TempatService {

    private final TempatRepository tempatRepository;
    private final ReviewRepository reviewRepository;
    private final FavoritRepository favoritRepository;

    public TempatService(TempatRepository tempatRepository,
                         ReviewRepository reviewRepository,
                         FavoritRepository favoritRepository) {
        this.tempatRepository = tempatRepository;
        this.reviewRepository = reviewRepository;
        this.favoritRepository = favoritRepository;
    }

    public List<Tempat> search(String keyword, String kategori, String lokasi, Integer hargaMin, Integer hargaMax, String fasilitas, String sort) {
        Specification<Tempat> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (hasText(keyword)) {
                String like = "%" + keyword.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("namaTempat")), like),
                        cb.like(cb.lower(root.get("alamat")), like),
                        cb.like(cb.lower(root.get("kategori")), like),
                        cb.like(cb.lower(root.get("deskripsi")), like)
                ));
            }
            if (hasText(kategori)) {
                predicates.add(cb.like(cb.lower(root.get("kategori")), "%" + kategori.toLowerCase() + "%"));
            }
            if (hasText(lokasi)) {
                predicates.add(cb.like(cb.lower(root.get("alamat")), "%" + lokasi.toLowerCase() + "%"));
            }
            if (hasText(fasilitas)) {
                predicates.add(cb.like(cb.lower(root.get("fasilitas")), "%" + fasilitas.toLowerCase() + "%"));
            }
            if (hargaMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("hargaMax"), hargaMin));
            }
            if (hargaMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("hargaMin"), hargaMax));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return tempatRepository.findAll(spec, toSort(sort));
    }

    public Tempat findById(Long id) {
        return tempatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tempat tidak ditemukan."));
    }

    @Transactional
    public Tempat create(TempatRequest request, User user) {
        validate(request);
        Tempat tempat = new Tempat();
        apply(request, tempat);
        tempat.setUser(user);
        return tempatRepository.save(tempat);
    }

    @Transactional
    public Tempat update(Long id, TempatRequest request) {
        validate(request);
        Tempat tempat = findById(id);
        apply(request, tempat);
        return tempatRepository.save(tempat);
    }

    @Transactional
    public void delete(Long id) {
        Tempat tempat = findById(id);
        reviewRepository.deleteByTempat(tempat);
        favoritRepository.deleteByTempat(tempat);
        tempatRepository.delete(tempat);
    }

    public TempatDto toDto(Tempat tempat, User currentUser) {
        double avg = reviewRepository.averageRatingByTempat(tempat);
        long count = reviewRepository.countByTempat(tempat);
        boolean fav = currentUser != null && favoritRepository.existsByUserAndTempat(currentUser, tempat);
        return TempatDto.from(tempat, Math.round(avg * 10.0) / 10.0, count, fav);
    }

    private void apply(TempatRequest request, Tempat tempat) {
        tempat.setNamaTempat(request.namaTempat().trim());
        tempat.setAlamat(blankToNull(request.alamat()));
        tempat.setDeskripsi(blankToNull(request.deskripsi()));
        tempat.setKategori(blankToNull(request.kategori()));
        tempat.setJamBuka(blankToNull(request.jamBuka()));
        tempat.setJamTutup(blankToNull(request.jamTutup()));
        tempat.setFoto(blankToNull(request.foto()));
        tempat.setFasilitas(blankToNull(request.fasilitas()));
        tempat.setHargaMin(request.hargaMin());
        tempat.setHargaMax(request.hargaMax());
    }

    private void validate(TempatRequest request) {
        if (request == null || !hasText(request.namaTempat())) {
            throw new BadRequestException("Nama tempat wajib diisi.");
        }
        if (request.hargaMin() != null && request.hargaMax() != null && request.hargaMin() > request.hargaMax()) {
            throw new BadRequestException("Harga minimum tidak boleh lebih besar dari harga maksimum.");
        }
    }

    private Sort toSort(String sort) {
        if ("nama_desc".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Direction.DESC, "namaTempat");
        }
        if ("kategori".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Direction.ASC, "kategori");
        }
        if ("rating".equalsIgnoreCase(sort)) {
            // Sorting rating dilakukan di sisi tampilan/API setelah DTO bila dibutuhkan.
            return Sort.by(Sort.Direction.ASC, "namaTempat");
        }
        return Sort.by(Sort.Direction.ASC, "namaTempat");
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String blankToNull(String value) {
        return hasText(value) ? value.trim() : null;
    }
}
