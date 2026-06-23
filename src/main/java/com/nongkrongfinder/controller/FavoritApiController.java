package com.nongkrongfinder.controller;

import com.nongkrongfinder.dto.FavoritDto;
import com.nongkrongfinder.entity.Favorit;
import com.nongkrongfinder.entity.User;
import com.nongkrongfinder.repository.UserRepository;
import com.nongkrongfinder.service.FavoritService;
import com.nongkrongfinder.service.TempatService;
import com.nongkrongfinder.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorit")
public class FavoritApiController {

    private final FavoritService favoritService;
    private final TempatService tempatService;
    private final UserRepository userRepository;

    public FavoritApiController(FavoritService favoritService, TempatService tempatService, UserRepository userRepository) {
        this.favoritService = favoritService;
        this.tempatService = tempatService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<FavoritDto> list(HttpSession session) {
        User user = SecurityUtil.requireLogin(session, userRepository);
        return favoritService.findByUser(user).stream()
                .map(fav -> FavoritDto.from(fav, tempatService.toDto(fav.getTempat(), user)))
                .toList();
    }

    @PostMapping("/{idTempat}")
    public FavoritDto add(@PathVariable Long idTempat, HttpSession session) {
        User user = SecurityUtil.requireLogin(session, userRepository);
        Favorit fav = favoritService.add(idTempat, user);
        return FavoritDto.from(fav, tempatService.toDto(fav.getTempat(), user));
    }

    @DeleteMapping("/{idFavorit}")
    public Map<String, Object> delete(@PathVariable Long idFavorit, HttpSession session) {
        User user = SecurityUtil.requireLogin(session, userRepository);
        favoritService.delete(idFavorit, user);
        return Map.of("success", true, "message", "Favorit berhasil dihapus.");
    }

    @DeleteMapping("/tempat/{idTempat}")
    public Map<String, Object> deleteByTempat(@PathVariable Long idTempat, HttpSession session) {
        User user = SecurityUtil.requireLogin(session, userRepository);
        favoritService.deleteByTempat(idTempat, user);
        return Map.of("success", true, "message", "Favorit berhasil dihapus.");
    }
}
