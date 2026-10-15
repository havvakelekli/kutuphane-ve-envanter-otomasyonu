package com.library.library_backend.controller;

import com.library.library_backend.entity.CezaOdemesi;
import com.library.library_backend.service.CezaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ceza")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CezaController {

    private final CezaService cezaService;

    @GetMapping("/kullanici/{kullaniciId}")
    public ResponseEntity<List<CezaOdemesi>> kullaniciCezalari(@PathVariable UUID kullaniciId) {
        return ResponseEntity.ok(cezaService.kullaniciCezalari(kullaniciId));
    }

    @PostMapping("/ode/{cezaId}")
    public ResponseEntity<CezaOdemesi> cezaOde(@PathVariable UUID cezaId) {
        return ResponseEntity.ok(cezaService.cezaOde(cezaId));
    }
}