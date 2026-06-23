package com.nongkrongfinder.service;

import com.nongkrongfinder.entity.Favorit;
import com.nongkrongfinder.entity.Tempat;
import com.nongkrongfinder.entity.User;
import com.nongkrongfinder.exception.NotFoundException;
import com.nongkrongfinder.repository.FavoritRepository;
import com.nongkrongfinder.repository.TempatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoritService {

    private final FavoritRepository favoritRepository;
    private final TempatRepository tempatRepository;

    public FavoritService(FavoritRepository favoritRepository, TempatRepository tempatRepository) {
        this.favoritRepository = favoritRepository;
        this.tempatRepository = tempatRepository;
    }

    public List<Favorit> findByUser(User user) {
        return favoritRepository.findByUserOrderByTanggalFavoritDesc(user);
    }

    @Transactional
    public Favorit add(Long idTempat, User user) {
        Tempat tempat = tempatRepository.findById(idTempat)
                .orElseThrow(() -> new NotFoundException("Tempat tidak ditemukan."));
        List<Favorit> existing = favoritRepository.findByUserAndTempat(user, tempat);
        if (!existing.isEmpty()) {
            return existing.get(0);
        }
        Favorit favorit = new Favorit();
        favorit.setUser(user);
        favorit.setTempat(tempat);
        return favoritRepository.save(favorit);
    }

    @Transactional
    public void delete(Long idFavorit, User user) {
        Favorit favorit = favoritRepository.findById(idFavorit)
                .orElseThrow(() -> new NotFoundException("Favorit tidak ditemukan."));
        if (!favorit.getUser().getIdUser().equals(user.getIdUser())) {
            throw new NotFoundException("Favorit tidak ditemukan.");
        }
        favoritRepository.delete(favorit);
    }

    @Transactional
    public void deleteByTempat(Long idTempat, User user) {
        Tempat tempat = tempatRepository.findById(idTempat)
                .orElseThrow(() -> new NotFoundException("Tempat tidak ditemukan."));
        favoritRepository.findByUserAndTempat(user, tempat).forEach(favoritRepository::delete);
    }
}
