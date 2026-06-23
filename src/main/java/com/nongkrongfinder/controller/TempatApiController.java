package com.nongkrongfinder.controller;

import com.nongkrongfinder.dto.TempatDto;
import com.nongkrongfinder.dto.TempatRequest;
import com.nongkrongfinder.entity.Tempat;
import com.nongkrongfinder.entity.User;
import com.nongkrongfinder.repository.UserRepository;
import com.nongkrongfinder.service.SearchService;
import com.nongkrongfinder.service.TempatService;
import com.nongkrongfinder.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/tempat")
public class TempatApiController {

    private final TempatService tempatService;
    private final SearchService searchService;
    private final UserRepository userRepository;

    public TempatApiController(TempatService tempatService, SearchService searchService, UserRepository userRepository) {
        this.tempatService = tempatService;
        this.searchService = searchService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<TempatDto> list(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String kategori,
                                @RequestParam(required = false) String lokasi,
                                @RequestParam(required = false) Integer hargaMin,
                                @RequestParam(required = false) Integer hargaMax,
                                @RequestParam(required = false) String fasilitas,
                                @RequestParam(required = false) String sort,
                                HttpSession session) {
        User current = currentUserOrNull(session);
        List<TempatDto> result = searchService.cariDanFilterTempat(keyword, kategori, lokasi, hargaMin, hargaMax, fasilitas, sort)
                .stream()
                .map(tempat -> tempatService.toDto(tempat, current))
                .toList();
        if ("rating".equalsIgnoreCase(sort)) {
            return result.stream()
                    .sorted(Comparator.comparing(TempatDto::ratingRataRata).reversed())
                    .toList();
        }
        return result;
    }

    @GetMapping("/{id}")
    public TempatDto detail(@PathVariable Long id, HttpSession session) {
        User current = currentUserOrNull(session);
        return tempatService.toDto(tempatService.findById(id), current);
    }

    @PostMapping
    public TempatDto create(@RequestBody TempatRequest request, HttpSession session) {
        User user = SecurityUtil.requireLogin(session, userRepository);
        Tempat tempat = tempatService.create(request, user);
        return tempatService.toDto(tempat, user);
    }

    @PutMapping("/{id}")
    public TempatDto update(@PathVariable Long id, @RequestBody TempatRequest request, HttpSession session) {
        Tempat existing = tempatService.findById(id);
        Long ownerId = existing.getUser() == null ? null : existing.getUser().getIdUser();
        SecurityUtil.requireOwnerOrAdmin(ownerId, session);
        Tempat updated = tempatService.update(id, request);
        return tempatService.toDto(updated, currentUserOrNull(session));
    }

    @DeleteMapping("/{id}")
    public java.util.Map<String, Object> delete(@PathVariable Long id, HttpSession session) {
        Tempat existing = tempatService.findById(id);
        Long ownerId = existing.getUser() == null ? null : existing.getUser().getIdUser();
        SecurityUtil.requireOwnerOrAdmin(ownerId, session);
        tempatService.delete(id);
        return java.util.Map.of("success", true, "message", "Tempat berhasil dihapus.");
    }

    private User currentUserOrNull(HttpSession session) {
        Long id = SecurityUtil.currentUserId(session);
        if (id == null) {
            return null;
        }
        return userRepository.findById(id).orElse(null);
    }
}
