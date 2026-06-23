package com.nongkrongfinder.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_nongkrong")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event")
    private Long idEvent;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "nama_event", nullable = false)
    private String namaEvent;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @Column(name = "tanggal_event")
    private LocalDateTime tanggalEvent;

    private String lokasi;

    @Column(name = "maks_peserta")
    private Integer maksPeserta;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null || status.isBlank()) {
            status = "AKTIF";
        }
    }

    public Long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Long idEvent) {
        this.idEvent = idEvent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNamaEvent() {
        return namaEvent;
    }

    public void setNamaEvent(String namaEvent) {
        this.namaEvent = namaEvent;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public LocalDateTime getTanggalEvent() {
        return tanggalEvent;
    }

    public void setTanggalEvent(LocalDateTime tanggalEvent) {
        this.tanggalEvent = tanggalEvent;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public Integer getMaksPeserta() {
        return maksPeserta;
    }

    public void setMaksPeserta(Integer maksPeserta) {
        this.maksPeserta = maksPeserta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
