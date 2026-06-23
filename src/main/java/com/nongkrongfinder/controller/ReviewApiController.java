package com.nongkrongfinder.controller;

import com.nongkrongfinder.dto.ReviewDto;
import com.nongkrongfinder.dto.ReviewRequest;
import com.nongkrongfinder.entity.Review;
import com.nongkrongfinder.entity.User;
import com.nongkrongfinder.repository.UserRepository;
import com.nongkrongfinder.service.ReviewService;
import com.nongkrongfinder.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/review")
public class ReviewApiController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;

    public ReviewApiController(ReviewService reviewService, UserRepository userRepository) {
        this.reviewService = reviewService;
        this.userRepository = userRepository;
    }

    @GetMapping("/tempat/{idTempat}")
    public Map<String, Object> listByTempat(@PathVariable Long idTempat, HttpSession session) {
        Long currentUserId = SecurityUtil.currentUserId(session);
        List<ReviewDto> reviews = reviewService.findByTempat(idTempat).stream()
                .map(review -> ReviewDto.from(review, currentUserId))
                .toList();
        return Map.of(
                "ratingRataRata", reviewService.averageRating(idTempat),
                "jumlahReview", reviews.size(),
                "reviews", reviews
        );
    }

    @PostMapping("/tempat/{idTempat}")
    public ReviewDto createOrUpdate(@PathVariable Long idTempat, @RequestBody ReviewRequest request, HttpSession session) {
        User user = SecurityUtil.requireLogin(session, userRepository);
        Review review = reviewService.createOrUpdate(idTempat, request, user);
        return ReviewDto.from(review, user.getIdUser());
    }

    @PutMapping("/{idReview}")
    public ReviewDto update(@PathVariable Long idReview, @RequestBody ReviewRequest request, HttpSession session) {
        Review existing = reviewService.findById(idReview);
        Long ownerId = existing.getUser() == null ? null : existing.getUser().getIdUser();
        SecurityUtil.requireOwnerOrAdmin(ownerId, session);
        Review updated = reviewService.update(idReview, request);
        return ReviewDto.from(updated, SecurityUtil.currentUserId(session));
    }

    @DeleteMapping("/{idReview}")
    public Map<String, Object> delete(@PathVariable Long idReview, HttpSession session) {
        Review existing = reviewService.findById(idReview);
        Long ownerId = existing.getUser() == null ? null : existing.getUser().getIdUser();
        SecurityUtil.requireOwnerOrAdmin(ownerId, session);
        reviewService.delete(idReview);
        return Map.of("success", true, "message", "Review berhasil dihapus.");
    }
}
