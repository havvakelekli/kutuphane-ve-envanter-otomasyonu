package com.library.library_backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KaynakBulunamadiException.class)
    public ResponseEntity<HataCevabi> handleKaynakBulunamadi(KaynakBulunamadiException ex, HttpServletRequest request) {
        HataCevabi hata = HataCevabi.builder()
                .zamanDamgasi(LocalDateTime.now())
                .durumKodu(HttpStatus.NOT_FOUND.value())
                .hata("KAYNAK_BULUNAMADI")
                .mesaj(ex.getMessage())
                .yol(request.getRequestURI())
                .build();
        return new ResponseEntity<>(hata, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(YetersizStokException.class)
    public ResponseEntity<HataCevabi> handleYetersizStok(YetersizStokException ex, HttpServletRequest request) {
        HataCevabi hata = HataCevabi.builder()
                .zamanDamgasi(LocalDateTime.now())
                .durumKodu(HttpStatus.BAD_REQUEST.value())
                .hata("YETERSIZ_STOK")
                .mesaj(ex.getMessage())
                .yol(request.getRequestURI())
                .build();
        return new ResponseEntity<>(hata, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AktifCezaBorcuException.class)
    public ResponseEntity<HataCevabi> handleAktifCeza(AktifCezaBorcuException ex, HttpServletRequest request) {
        HataCevabi hata = HataCevabi.builder()
                .zamanDamgasi(LocalDateTime.now())
                .durumKodu(HttpStatus.BAD_REQUEST.value())
                .hata("AKTIF_CEZA_BORCU")
                .mesaj(ex.getMessage())
                .yol(request.getRequestURI())
                .build();
        return new ResponseEntity<>(hata, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GecersizIslemException.class)
    public ResponseEntity<HataCevabi> handleGecersizIslem(GecersizIslemException ex, HttpServletRequest request) {
        HataCevabi hata = HataCevabi.builder()
                .zamanDamgasi(LocalDateTime.now())
                .durumKodu(HttpStatus.BAD_REQUEST.value())
                .hata("GECERSIZ_ISLEM")
                .mesaj(ex.getMessage())
                .yol(request.getRequestURI())
                .build();
        return new ResponseEntity<>(hata, HttpStatus.BAD_REQUEST);
    }

    // Form doğrulama hataları (@Valid @NotBlank vs.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> hatalar = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String alanAdi = ((FieldError) error).getField();
            String hataMesaji = error.getDefaultMessage();
            hatalar.put(alanAdi, hataMesaji);
        });
        return new ResponseEntity<>(hatalar, HttpStatus.BAD_REQUEST);
    }

    // Genel Beklenmeyen Hatalar
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HataCevabi> handleGeneralException(Exception ex, HttpServletRequest request) {
        HataCevabi hata = HataCevabi.builder()
                .zamanDamgasi(LocalDateTime.now())
                .durumKodu(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .hata("SUNUCU_HATASI")
                .mesaj(ex.getMessage())
                .yol(request.getRequestURI())
                .build();
        return new ResponseEntity<>(hata, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}