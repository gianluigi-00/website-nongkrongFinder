package com.nongkrongfinder.service;

import com.nongkrongfinder.entity.Tempat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final TempatService tempatService;

    public SearchService(TempatService tempatService) {
        this.tempatService = tempatService;
    }

    public List<Tempat> cariDanFilterTempat(String keyword, String kategori, String lokasi,
                                             Integer hargaMin, Integer hargaMax, String fasilitas, String sort) {
        return tempatService.search(keyword, kategori, lokasi, hargaMin, hargaMax, fasilitas, sort);
    }
}
