package com.library.library_backend.controller;

import com.library.library_backend.dto.auth.AuthCevabi;
import com.library.library_backend.dto.auth.GirisIstegi;
import com.library.library_backend.dto.auth.KayitIstegi;
import com.library.library_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Frontend'den gelen isteklere izin veriyoruz
public class AuthController {

    private final AuthService authService;

    @PostMapping("/kayit-ol")
    public ResponseEntity<AuthCevabi> kayitOl(@Valid @RequestBody KayitIstegi istek) {
        return ResponseEntity.ok(authService.kayitOl(istek));
    }

    @PostMapping("/giris-yap")
    public ResponseEntity<AuthCevabi> girisYap(@Valid @RequestBody GirisIstegi istek) {
        return ResponseEntity.ok(authService.girisYap(istek));
    }
}