package com.nongkrongfinder.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_review")
    private Long idReview;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_tempat")
    private Tempat tempat;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String komentar;

    @Column(name = "tanggal_review")
    private LocalDateTime tanggalReview;

    @PrePersist
    public void prePersist() {
        if (tanggalReview == null) {
            tanggalReview = LocalDateTime.now();
        }
    }

    public Long getIdReview() {
        return idReview;
    }

    public void setIdReview(Long idReview) {
        this.idReview = idReview;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tempat getTempat() {
        return tempat;
    }

    public void setTempat(Tempat tempat) {
        this.tempat = tempat;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public LocalDateTime getTanggalReview() {
        return tanggalReview;
    }

    public void setTanggalReview(LocalDateTime tanggalReview) {
        this.tanggalReview = tanggalReview;
    }
}
