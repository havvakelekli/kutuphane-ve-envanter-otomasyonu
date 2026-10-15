package com.library.library_backend.controller;

import com.library.library_backend.entity.Rezervasyon;
import com.library.library_backend.service.RezervasyonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rezervasyon")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RezervasyonController {

    private final RezervasyonService rezervasyonService;

    @PostMapping("/yap")
    public ResponseEntity<Rezervasyon> rezervasyonYap(@RequestParam UUID kullaniciId, @RequestParam UUID kitapId) {
        return ResponseEntity.ok(rezervasyonService.rezervasyonYap(kullaniciId, kitapId));
    }

    @GetMapping("/kullanici/{kullaniciId}")
    public ResponseEntity<List<Rezervasyon>> kullaniciRezervasyonlari(@PathVariable UUID kullaniciId) {
        return ResponseEntity.ok(rezervasyonService.kullaniciRezervasyonlari(kullaniciId));
    }
}