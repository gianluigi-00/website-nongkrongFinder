package com.nongkrongfinder.repository;

import com.nongkrongfinder.entity.Review;
import com.nongkrongfinder.entity.Tempat;
import com.nongkrongfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTempatOrderByTanggalReviewDesc(Tempat tempat);
    List<Review> findByUser(User user);
    List<Review> findByUserAndTempatOrderByTanggalReviewDesc(User user, Tempat tempat);
    long countByTempat(Tempat tempat);
    void deleteByTempat(Tempat tempat);
    void deleteByUser(User user);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.tempat = :tempat")
    double averageRatingByTempat(@Param("tempat") Tempat tempat);
}
