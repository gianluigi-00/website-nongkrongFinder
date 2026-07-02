package com.nongkrongfinder.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorit")
public class Favorit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favorit")
    private Long idFavorit;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_tempat")
    private Tempat tempat;

    @Column(name = "tanggal_favorit")
    private LocalDateTime tanggalFavorit;

    @PrePersist
    public void prePersist() {
        if (tanggalFavorit == null) {
            tanggalFavorit = LocalDateTime.now();
        }
    }

    public Long getIdFavorit() {
        return idFavorit;
    }

    public void setIdFavorit(Long idFavorit) {
        this.idFavorit = idFavorit;
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

    public LocalDateTime getTanggalFavorit() {
        return tanggalFavorit;
    }

    public void setTanggalFavorit(LocalDateTime tanggalFavorit) {
        this.tanggalFavorit = tanggalFavorit;
    }
}