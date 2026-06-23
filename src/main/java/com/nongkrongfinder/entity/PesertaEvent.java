package com.nongkrongfinder.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "peserta_event")
public class PesertaEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_peserta")
    private Long idPeserta;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_event")
    private Event event;

    @Column(name = "tanggal_join")
    private LocalDateTime tanggalJoin;

    @PrePersist
    public void prePersist() {
        if (tanggalJoin == null) {
            tanggalJoin = LocalDateTime.now();
        }
    }

    public Long getIdPeserta() {
        return idPeserta;
    }

    public void setIdPeserta(Long idPeserta) {
        this.idPeserta = idPeserta;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getTanggalJoin() {
        return tanggalJoin;
    }

    public void setTanggalJoin(LocalDateTime tanggalJoin) {
        this.tanggalJoin = tanggalJoin;
    }
}
