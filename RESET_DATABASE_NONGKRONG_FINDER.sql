-- Jalankan file ini di MySQL/phpMyAdmin kalau masih muncul error tabel/kolom lama.
-- PERHATIAN: script ini menghapus database nongkrong_finder lalu membuat ulang dari awal.

DROP DATABASE IF EXISTS nongkrong_finder;
CREATE DATABASE nongkrong_finder CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE nongkrong_finder;

CREATE TABLE users (
  id_user BIGINT NOT NULL AUTO_INCREMENT,
  nama VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(10) NOT NULL DEFAULT 'USER',
  foto_profil VARCHAR(255),
  created_at DATETIME(6),
  PRIMARY KEY (id_user)
);

CREATE TABLE tempat (
  id_tempat BIGINT NOT NULL AUTO_INCREMENT,
  id_user BIGINT,
  nama_tempat VARCHAR(255) NOT NULL,
  alamat VARCHAR(255),
  deskripsi TEXT,
  kategori VARCHAR(255),
  jam_buka VARCHAR(255),
  jam_tutup VARCHAR(255),
  foto VARCHAR(1000),
  fasilitas TEXT,
  harga_min INT,
  harga_max INT,
  created_at DATETIME(6),
  PRIMARY KEY (id_tempat),
  CONSTRAINT fk_tempat_user FOREIGN KEY (id_user) REFERENCES users(id_user)
);

CREATE TABLE review (
  id_review BIGINT NOT NULL AUTO_INCREMENT,
  id_user BIGINT,
  id_tempat BIGINT,
  rating INT,
  komentar TEXT,
  tanggal_review DATETIME(6),
  PRIMARY KEY (id_review),
  CONSTRAINT fk_review_user FOREIGN KEY (id_user) REFERENCES users(id_user),
  CONSTRAINT fk_review_tempat FOREIGN KEY (id_tempat) REFERENCES tempat(id_tempat)
);

CREATE TABLE favorit (
  id_favorit BIGINT NOT NULL AUTO_INCREMENT,
  id_user BIGINT,
  id_tempat BIGINT,
  tanggal_favorit DATETIME(6),
  PRIMARY KEY (id_favorit),
  CONSTRAINT fk_favorit_user FOREIGN KEY (id_user) REFERENCES users(id_user),
  CONSTRAINT fk_favorit_tempat FOREIGN KEY (id_tempat) REFERENCES tempat(id_tempat),
  CONSTRAINT uk_favorit_user_tempat UNIQUE (id_user, id_tempat)
);

CREATE TABLE event_nongkrong (
  id_event BIGINT NOT NULL AUTO_INCREMENT,
  id_user BIGINT,
  nama_event VARCHAR(255) NOT NULL,
  deskripsi TEXT,
  tanggal_event DATETIME(6),
  lokasi VARCHAR(255),
  maks_peserta INT,
  status VARCHAR(255),
  created_at DATETIME(6),
  PRIMARY KEY (id_event),
  CONSTRAINT fk_event_user FOREIGN KEY (id_user) REFERENCES users(id_user)
);

CREATE TABLE peserta_event (
  id_peserta BIGINT NOT NULL AUTO_INCREMENT,
  id_user BIGINT,
  id_event BIGINT,
  tanggal_join DATETIME(6),
  PRIMARY KEY (id_peserta),
  CONSTRAINT fk_peserta_user FOREIGN KEY (id_user) REFERENCES users(id_user),
  CONSTRAINT fk_peserta_event FOREIGN KEY (id_event) REFERENCES event_nongkrong(id_event),
  CONSTRAINT uk_peserta_user_event UNIQUE (id_user, id_event)
);

INSERT INTO users (nama, email, password, role, created_at)
VALUES ('Administrator', 'admin@gmail.com', 'admin123', 'ADMIN', NOW(6));
