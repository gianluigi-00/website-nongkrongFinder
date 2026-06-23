# Nongkrong Finder - Update Tanpa Thymeleaf

Versi ini dikembangkan dari project `nongkrong-finder-main_2.zip` dengan pendekatan:

- Java 17
- Spring Boot MVC + REST Controller
- Spring Data JPA
- MySQL
- View berupa static HTML, CSS, dan JavaScript di `src/main/resources/static`
- Tidak memakai Thymeleaf dan tidak ada folder `templates`

## Cara Menjalankan

1. Buat database MySQL bernama `nongkrong` atau biarkan aplikasi membuat otomatis melalui URL JDBC.
2. Pastikan konfigurasi ada di `src/main/resources/application.properties`.
3. Jalankan:

```bash
./mvnw spring-boot:run
```

Jika di Windows:

```bash
mvnw.cmd spring-boot:run
```

4. Buka browser:

```text
http://localhost:8080
```

## Akun Admin Default

Saat aplikasi pertama kali jalan, sistem otomatis membuat admin jika belum ada:

```text
Email    : admin@gmail.com
Password : admin123
```

## Halaman User

- `/dashboard`
- `/tempat`
- `/tempat-form`
- `/tempat-detail?id=ID_TEMPAT`
- `/review?tempatId=ID_TEMPAT`
- `/favorit`
- `/event`
- `/event-form`
- `/event-peserta?id=ID_EVENT`
- `/profil`

## Halaman Admin

- `/admin/dashboard`
- `/admin/users`
- `/admin/tempat`
- `/admin/review`
- `/admin/event`

## API Utama

- Auth: `/api/auth/*`
- Tempat: `/api/tempat/*`
- Review: `/api/review/*`
- Favorit: `/api/favorit/*`
- Event: `/api/event/*`
- Admin: `/api/admin/*`

## Perbaikan Penting

1. Thymeleaf dihapus dari `pom.xml`.
2. View dipindahkan ke static HTML + JavaScript.
3. Review diperbaiki:
   - Review tampil berdasarkan tempat, bukan `findAll()`.
   - User hanya punya satu review aktif per tempat; jika submit lagi, review diperbarui.
   - Rating divalidasi 1 sampai 5.
   - Edit dan hapus review bisa dilakukan oleh pemilik atau admin.
4. Favorit diperbaiki:
   - Tidak membuat favorit dobel untuk tempat yang sama.
   - Hapus favorit hanya boleh oleh pemilik favorit.
5. Event diperbaiki:
   - Join event dicegah dobel.
   - Kapasitas maksimal peserta dicek.
   - User bisa keluar event.
   - Pemilik event atau admin bisa edit/hapus event.
6. Admin dipisah:
   - Kelola user.
   - Kelola tempat.
   - Kelola review.
   - Kelola event.
7. Delete data dibuat aman dari foreign key:
   - Hapus tempat ikut membersihkan review dan favorit.
   - Hapus event ikut membersihkan peserta.
   - Hapus user ikut membersihkan data terkait.

## Pembagian File per Anggota

### Rikza Nur Zaki - Manajemen User

- `AuthApiController.java`
- `AdminApiController.java` bagian users
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
- `TempatRepository.java`
- Form filter pada `tempat.html`

### Gianluigi Andreas Putra Butarbutar - Favorit / Bookmark

- `FavoritApiController.java`
- `FavoritService.java`
- `Favorit.java`
- `FavoritRepository.java`
- `favorit.html`
- Tombol favorit di `tempat.html`

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
