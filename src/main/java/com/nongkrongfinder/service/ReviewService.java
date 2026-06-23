package com.nongkrongfinder.service;

import com.nongkrongfinder.dto.ReviewRequest;
import com.nongkrongfinder.entity.Review;
import com.nongkrongfinder.entity.Tempat;
import com.nongkrongfinder.entity.User;
import com.nongkrongfinder.exception.BadRequestException;
import com.nongkrongfinder.exception.NotFoundException;
import com.nongkrongfinder.repository.ReviewRepository;
import com.nongkrongfinder.repository.TempatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TempatRepository tempatRepository;

    public ReviewService(ReviewRepository reviewRepository, TempatRepository tempatRepository) {
        this.reviewRepository = reviewRepository;
        this.tempatRepository = tempatRepository;
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review tidak ditemukan."));
    }

    public List<Review> findByTempat(Long idTempat) {
        Tempat tempat = tempatRepository.findById(idTempat)
                .orElseThrow(() -> new NotFoundException("Tempat tidak ditemukan."));
        return reviewRepository.findByTempatOrderByTanggalReviewDesc(tempat);
    }

    @Transactional
    public Review createOrUpdate(Long idTempat, ReviewRequest request, User user) {
        validate(request);
        Tempat tempat = tempatRepository.findById(idTempat)
                .orElseThrow(() -> new NotFoundException("Tempat tidak ditemukan."));

        List<Review> existing = reviewRepository.findByUserAndTempatOrderByTanggalReviewDesc(user, tempat);
        Review review = existing.isEmpty() ? new Review() : existing.get(0);
        review.setUser(user);
        review.setTempat(tempat);
        review.setRating(request.rating());
        review.setKomentar(request.komentar().trim());
        if (review.getIdReview() != null) {
            review.setTanggalReview(LocalDateTime.now());
        }
        return reviewRepository.save(review);
    }

    @Transactional
    public Review update(Long idReview, ReviewRequest request) {
        validate(request);
        Review review = findById(idReview);
        review.setRating(request.rating());
        review.setKomentar(request.komentar().trim());
        review.setTanggalReview(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    @Transactional
    public void delete(Long idReview) {
        Review review = findById(idReview);
        reviewRepository.delete(review);
    }

    public double averageRating(Long idTempat) {
        Tempat tempat = tempatRepository.findById(idTempat)
                .orElseThrow(() -> new NotFoundException("Tempat tidak ditemukan."));
        double avg = reviewRepository.averageRatingByTempat(tempat);
        return Math.round(avg * 10.0) / 10.0;
    }

    private void validate(ReviewRequest request) {
        if (request == null) {
            throw new BadRequestException("Data review tidak boleh kosong.");
        }
        if (request.rating() == null || request.rating() < 1 || request.rating() > 5) {
            throw new BadRequestException("Rating wajib bernilai 1 sampai 5.");
        }
        if (request.komentar() == null || request.komentar().isBlank()) {
            throw new BadRequestException("Komentar wajib diisi.");
        }
    }
}
