package com.nongkrongfinder.repository;

import com.nongkrongfinder.entity.Favorit;
import com.nongkrongfinder.entity.Tempat;
import com.nongkrongfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritRepository extends JpaRepository<Favorit, Long> {
    List<Favorit> findByUserOrderByTanggalFavoritDesc(User user);
    List<Favorit> findByUser(User user);
    List<Favorit> findByUserAndTempat(User user, Tempat tempat);
    boolean existsByUserAndTempat(User user, Tempat tempat);
    void deleteByTempat(Tempat tempat);
    void deleteByUser(User user);
}
