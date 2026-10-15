package com.library.library_backend.controller;

import com.library.library_backend.entity.OduncDurumu;
import com.library.library_backend.repository.CezaOdemesiRepository;
import com.library.library_backend.repository.KitapRepository;
import com.library.library_backend.repository.KullaniciRepository;
import com.library.library_backend.repository.OduncKaydiRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/istatistik")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IstatistikController {

    private final KitapRepository kitapRepository;
    private final KullaniciRepository kullaniciRepository;
    private final OduncKaydiRepository oduncKaydiRepository;
    private final CezaOdemesiRepository cezaOdemesiRepository;

    @Data
    @Builder
    public static class DashboardOzeti {
        private long toplamKitapCesidi;
        private long toplamUyeSayisi;
        private long suAnOduncteOlanlar;
        private long gecikmisKitaplar;
    }

    @GetMapping("/ozet")
    public ResponseEntity<DashboardOzeti> getOzet() {
        long oduncte = oduncKaydiRepository.findAll().stream()
                .filter(o -> o.getDurum() == OduncDurumu.ODUNC_ALINDI)
                .count();

        long geciken = oduncKaydiRepository.findByDurumAndSonTeslimTarihiBefore(OduncDurumu.ODUNC_ALINDI, LocalDateTime.now()).size();

        DashboardOzeti ozet = DashboardOzeti.builder()
                .toplamKitapCesidi(kitapRepository.count())
                .toplamUyeSayisi(kullaniciRepository.count())
                .suAnOduncteOlanlar(oduncte)
                .gecikmisKitaplar(geciken)
                .build();

        return ResponseEntity.ok(ozet);
    }
}