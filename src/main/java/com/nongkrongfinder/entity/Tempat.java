package com.nongkrongfinder.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tempat")
public class Tempat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tempat")
    private Long idTempat;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "nama_tempat", nullable = false)
    private String namaTempat;

    private String alamat;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    private String kategori;

    @Column(name = "jam_buka")
    private String jamBuka;

    @Column(name = "jam_tutup")
    private String jamTutup;

    @Column(name = "gambar")
    private String foto;

    @Column(columnDefinition = "TEXT")
    private String fasilitas;

    @Column(name = "harga_min")
    private Integer hargaMin;

    @Column(name = "harga_max")
    private Integer hargaMax;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getIdTempat() {
        return idTempat;
    }

    public void setIdTempat(Long idTempat) {
        this.idTempat = idTempat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNamaTempat() {
        return namaTempat;
    }

    public void setNamaTempat(String namaTempat) {
        this.namaTempat = namaTempat;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getJamBuka() {
        return jamBuka;
    }

    public void setJamBuka(String jamBuka) {
        this.jamBuka = jamBuka;
    }

    public String getJamTutup() {
        return jamTutup;
    }

    public void setJamTutup(String jamTutup) {
        this.jamTutup = jamTutup;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    public Integer getHargaMin() {
        return hargaMin;
    }

    public void setHargaMin(Integer hargaMin) {
        this.hargaMin = hargaMin;
    }

    public Integer getHargaMax() {
        return hargaMax;
    }

    public void setHargaMax(Integer hargaMax) {
        this.hargaMax = hargaMax;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
