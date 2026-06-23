# Fix Admin Dashboard, Login/Register, dan Foto Profil

## Penyebab error admin dashboard 500

Error di `/admin/dashboard` terjadi karena `PageController` lama memakai mapping umum:

```java
@GetMapping("/admin/{page}")
```

Saat controller melakukan `forward:/admin/dashboard.html`, URL `.html` ikut tertangkap lagi oleh mapping itu, sehingga terjadi forward berulang dan muncul HTTP Status 500.

## Perbaikan

`PageController` sekarang memakai mapping eksplisit:

- `/admin/dashboard` -> `/admin/dashboard.html`
- `/admin/users` -> `/admin/users.html`
- `/admin/tempat` -> `/admin/tempat.html`
- `/admin/review` -> `/admin/review.html`
- `/admin/event` -> `/admin/event.html`

Jadi tidak ada loop lagi.

## Login/Register

Tampilan login dan register sudah dibuat fokus ke form saja:

- panel gambar/hero sebelah kiri dihapus
- admin default tidak ditampilkan di halaman
- form dibuat center dan lebih bersih

Admin default tetap dibuat otomatis oleh sistem:

```text
admin@gmail.com
admin123
```

## Foto profil

Di halaman `/profil`, URL foto profil sekarang langsung tampil sebagai preview.

Gunakan link gambar langsung, misalnya:

```text
https://i.imgur.com/contoh.jpg
https://domain.com/foto.png
https://domain.com/avatar.webp
```

Jangan gunakan link Google Search, karena itu bukan link file gambar langsung.

Kalau URL salah/rusak, aplikasi otomatis menampilkan avatar default berdasarkan nama user.

## Setelah update ZIP

1. Stop aplikasi Spring Boot yang sedang berjalan.
2. Extract ZIP terbaru.
3. Jalankan ulang:

```bash
mvnw.cmd spring-boot:run
```

4. Buka:

```text
http://localhost:8080/login
```

Jika database masih bentrok, jalankan:

```sql
DROP DATABASE IF EXISTS nongkrong_finder;
CREATE DATABASE nongkrong_finder;
```
