# Fix Final Database + UI

## Kenapa error sebelumnya muncul?

### 1. `Table 'nongkrong.user' doesn't exist`
Project sebelumnya memakai tabel bernama `user`. Di MySQL, `user` rawan bentrok karena MySQL juga punya konsep/tabel sistem bernama user. Selain itu, database yang dipakai masih `nongkrong`, sedangkan struktur tabel lama tidak sinkron dengan entity Java.

Di versi ini tabel user diganti menjadi:

```text
users
```

### 2. `Unknown column 'fasilitas' in 'field list'`
Kode Java sudah meminta kolom `fasilitas`, `harga_min`, dan `harga_max`, tetapi tabel `tempat` di database lama belum punya kolom itu. Karena database lama masih dipakai, Hibernate tidak selalu berhasil menyamakan struktur jika tabel dibuat manual/versi lama.

Di versi ini default database diganti menjadi:

```text
nongkrong_finder
```

Supaya tidak bentrok dengan database lama `nongkrong`.

## Setting database yang benar

Pastikan MySQL aktif, lalu pakai setting default berikut di `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nongkrong_finder?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

Kalau password MySQL kamu tidak kosong, isi bagian:

```properties
spring.datasource.password=password_kamu
```

## Cara paling aman reset database

Jalankan file:

```text
RESET_DATABASE_NONGKRONG_FINDER.sql
```

Atau jalankan manual di MySQL/phpMyAdmin:

```sql
DROP DATABASE IF EXISTS nongkrong_finder;
CREATE DATABASE nongkrong_finder;
```

Lalu jalankan aplikasi lagi:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

## Admin default

Admin tetap otomatis dibuat oleh program, tetapi tidak ditampilkan lagi di halaman login/register.

```text
Email    : admin@gmail.com
Password : admin123
```

## Yang diperbaiki

- Tabel `user` diganti menjadi `users`.
- Tabel `event` diganti menjadi `event_nongkrong`.
- Database default diganti dari `nongkrong` ke `nongkrong_finder`.
- Field `fasilitas`, `harga_min`, `harga_max` sudah sinkron dengan entity `Tempat`.
- Tampilan login/register dibuat ulang agar lebih rapi.
- Tampilan admin default di login/register dihapus.
