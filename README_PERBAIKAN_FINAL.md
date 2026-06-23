# Nongkrong Finder - Final Update (UI Profesional + Fix Error)

Versi ini melanjutkan project dari ZIP sebelumnya, tetapi sudah diperbaiki dan diarahkan ke:

- Java 17
- Spring Boot 3.3.x
- Arsitektur MVC (Controller + Service + Repository + Entity)
- Database MySQL
- View **tanpa Thymeleaf**
- Static HTML + CSS + JavaScript (REST API)
- UI/UX dibuat lebih rapi dan profesional

## Fokus perbaikan utama

### 1) Login admin error
Sudah diperbaiki dengan pendekatan session yang aman:
- Session hanya menyimpan `userId` dan `role`
- Tidak menyimpan object `User` penuh ke session
- Admin default otomatis dibuat oleh `DataSeeder`

**Admin default:**
- Email: `admin@gmail.com`
- Password: `admin123`

### 2) Tambah tempat error
Sudah diperbaiki dengan:
- Entity `Tempat` sudah memiliki field yang lengkap:
  - `fasilitas`
  - `hargaMin`
  - `hargaMax`
  - `createdAt`
  - relasi `user`
- `spring.jpa.hibernate.ddl-auto=update` diaktifkan agar kolom baru dibuat otomatis saat aplikasi dijalankan
- Validasi form tambah/edit tempat diperbaiki

### 3) Filter tempat error
Sudah diperbaiki dengan filter berbasis `Specification` JPA dan field entity yang sesuai:
- keyword
- kategori
- lokasi
- fasilitas
- hargaMin
- hargaMax
- sort nama / kategori / rating

### 4) UI/UX profesional
Sudah diperbarui:
- halaman login/register lebih modern
- dashboard lebih rapi
- komponen kartu, tabel, form, badge, dan navigation ditata ulang
- responsive untuk layar laptop dan mobile

## Cara menjalankan

1. Pastikan MySQL aktif.
2. Buat database `nongkrong` (atau biarkan otomatis dibuat oleh JDBC URL).
3. Jalankan:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

4. Buka:

```text
http://localhost:8080
```

## Jika database lama masih bermasalah
Kalau sebelumnya database sudah berisi tabel lama yang strukturnya rusak / tidak sinkron, lakukan salah satu:

### Opsi A (disarankan untuk demo cepat)
Drop database lama lalu jalankan ulang aplikasi:

```sql
DROP DATABASE nongkrong;
CREATE DATABASE nongkrong;
```

### Opsi B
Biarkan `ddl-auto=update` menambah kolom baru otomatis.

## Halaman utama

### User
- `/login`
- `/register`
- `/dashboard`
- `/tempat`
- `/tempat-form`
- `/tempat-detail?id=1`
- `/review?tempatId=1`
- `/favorit`
- `/event`
- `/event-form`
- `/event-peserta?id=1`
- `/profil`

### Admin
- `/admin/dashboard`
- `/admin/users`
- `/admin/tempat`
- `/admin/review`
- `/admin/event`

## Pembagian modul per anggota

### Rikza Nur Zaki - Manajemen User
- `AuthApiController.java`
- `AdminApiController.java` (users)
- `UserService.java`
- `User.java`
- `UserRepository.java`
- `login.html`
- `register.html`
- `profil.html`
- `admin/users.html`

### Muhammad Ihsan Sinaga - Manajemen Tempat Nongkrong
- `TempatApiController.java`
- `TempatService.java`
- `Tempat.java`
- `TempatRepository.java`
- `tempat.html`
- `tempat-form.html`
- `tempat-detail.html`
- `admin/tempat.html`

### Syahdan Awal Ramadhan - Review & Rating
- `ReviewApiController.java`
- `ReviewService.java`
- `Review.java`
- `ReviewRepository.java`
- `review.html`
- `admin/review.html`

### Rafly Adinata Prayoga - Pencarian & Filter
- `SearchService.java`
- `TempatService.java` method `search(...)`
- form filter pada `tempat.html`

### Gianluigi Andreas Putra Butarbutar - Favorit (Bookmark)
- `FavoritApiController.java`
- `FavoritService.java`
- `Favorit.java`
- `FavoritRepository.java`
- `favorit.html`

### Luthfi Maolana Andhika Widyadana - Event Nongkrong
- `EventApiController.java`
- `EventService.java`
- `Event.java`
- `PesertaEvent.java`
- `EventRepository.java`
- `PesertaEventRepository.java`
- `event.html`
- `event-form.html`
- `event-peserta.html`
- `admin/event.html`
