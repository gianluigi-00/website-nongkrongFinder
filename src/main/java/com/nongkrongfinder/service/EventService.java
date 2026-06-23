package com.nongkrongfinder.service;

import com.nongkrongfinder.dto.EventDto;
import com.nongkrongfinder.dto.EventRequest;
import com.nongkrongfinder.entity.Event;
import com.nongkrongfinder.entity.PesertaEvent;
import com.nongkrongfinder.entity.User;
import com.nongkrongfinder.exception.BadRequestException;
import com.nongkrongfinder.exception.NotFoundException;
import com.nongkrongfinder.repository.EventRepository;
import com.nongkrongfinder.repository.PesertaEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final PesertaEventRepository pesertaEventRepository;

    public EventService(EventRepository eventRepository, PesertaEventRepository pesertaEventRepository) {
        this.eventRepository = eventRepository;
        this.pesertaEventRepository = pesertaEventRepository;
    }

    public List<Event> findAll() {
        return eventRepository.findAll().stream()
                .sorted(Comparator.comparing(Event::getTanggalEvent, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event tidak ditemukan."));
    }

    @Transactional
    public Event create(EventRequest request, User user) {
        validate(request);
        Event event = new Event();
        apply(request, event);
        event.setUser(user);
        if (event.getStatus() == null || event.getStatus().isBlank()) {
            event.setStatus("AKTIF");
        }
        return eventRepository.save(event);
    }

    @Transactional
    public Event update(Long id, EventRequest request) {
        validate(request);
        Event event = findById(id);
        apply(request, event);
        return eventRepository.save(event);
    }

    @Transactional
    public void delete(Long id) {
        Event event = findById(id);
        pesertaEventRepository.deleteByEvent(event);
        eventRepository.delete(event);
    }

    @Transactional
    public PesertaEvent join(Long idEvent, User user) {
        Event event = findById(idEvent);
        if (!"AKTIF".equalsIgnoreCase(event.getStatus())) {
            throw new BadRequestException("Event tidak aktif, tidak bisa join.");
        }
        if (pesertaEventRepository.existsByUserAndEvent(user, event)) {
            throw new BadRequestException("Kamu sudah join event ini.");
        }
        long jumlahPeserta = pesertaEventRepository.countByEvent(event);
        if (event.getMaksPeserta() != null && event.getMaksPeserta() > 0 && jumlahPeserta >= event.getMaksPeserta()) {
            throw new BadRequestException("Peserta event sudah penuh.");
        }
        PesertaEvent peserta = new PesertaEvent();
        peserta.setUser(user);
        peserta.setEvent(event);
        return pesertaEventRepository.save(peserta);
    }

    @Transactional
    public void leave(Long idEvent, User user) {
        Event event = findById(idEvent);
        pesertaEventRepository.findByUserAndEvent(user, event).forEach(pesertaEventRepository::delete);
    }

    public List<PesertaEvent> peserta(Long idEvent) {
        Event event = findById(idEvent);
        return pesertaEventRepository.findByEvent(event);
    }

    public EventDto toDto(Event event, User currentUser) {
        long count = pesertaEventRepository.countByEvent(event);
        boolean joined = currentUser != null && pesertaEventRepository.existsByUserAndEvent(currentUser, event);
        Long currentId = currentUser == null ? null : currentUser.getIdUser();
        return EventDto.from(event, count, joined, currentId);
    }

    private void apply(EventRequest request, Event event) {
        event.setNamaEvent(request.namaEvent().trim());
        event.setDeskripsi(blankToNull(request.deskripsi()));
        event.setTanggalEvent(request.tanggalEvent());
        event.setLokasi(blankToNull(request.lokasi()));
        event.setMaksPeserta(request.maksPeserta());
        event.setStatus(normalizeStatus(request.status()));
    }

    private void validate(EventRequest request) {
        if (request == null || request.namaEvent() == null || request.namaEvent().isBlank()) {
            throw new BadRequestException("Nama event wajib diisi.");
        }
        if (request.maksPeserta() != null && request.maksPeserta() < 0) {
            throw new BadRequestException("Maksimal peserta tidak boleh negatif.");
        }
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return "AKTIF";
        }
        String normalized = status.trim().toUpperCase();
        if (!normalized.equals("AKTIF") && !normalized.equals("SELESAI") && !normalized.equals("BATAL")) {
            throw new BadRequestException("Status event hanya boleh AKTIF, SELESAI, atau BATAL.");
        }
        return normalized;
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
