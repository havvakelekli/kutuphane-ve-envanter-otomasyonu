package com.library.library_backend.controller;

import com.library.library_backend.dto.kitap.KitapCevabi;
import com.library.library_backend.dto.kitap.KitapIstegi;
import com.library.library_backend.service.KitapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/kitaplar")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class KitapController {

    private final KitapService kitapService;

    // Kitap ekleme (Admin ve Kütüphaneci yetkisi)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROL_ADMIN', 'ROL_KUTUPHANECI')")
    public ResponseEntity<KitapCevabi> kitapEkle(@Valid @RequestBody KitapIstegi istek) {
        return ResponseEntity.ok(kitapService.kitapEkle(istek));
    }

    // Tüm kitapları getirme (Herkese açık)
    @GetMapping
    public ResponseEntity<List<KitapCevabi>> tumKitaplar() {
        return ResponseEntity.ok(kitapService.tumKitaplar());
    }

    // Arama
    @GetMapping("/ara")
    public ResponseEntity<List<KitapCevabi>> kitapAra(@RequestParam String kelime) {
        return ResponseEntity.ok(kitapService.kitapAra(kelime));
    }

    // Tek kitap detayı
    @GetMapping("/{id}")
    public ResponseEntity<KitapCevabi> kitapGetir(@PathVariable UUID id) {
        return ResponseEntity.ok(kitapService.kitapGetir(id));
    }
}