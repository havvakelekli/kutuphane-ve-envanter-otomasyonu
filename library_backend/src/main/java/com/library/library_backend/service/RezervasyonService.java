package com.library.library_backend.service;

import com.library.library_backend.entity.Kitap;
import com.library.library_backend.entity.Kullanici;
import com.library.library_backend.entity.Rezervasyon;
import com.library.library_backend.entity.RezervasyonDurumu;
import com.library.library_backend.exception.GecersizIslemException;
import com.library.library_backend.exception.KaynakBulunamadiException;
import com.library.library_backend.repository.KitapRepository;
import com.library.library_backend.repository.KullaniciRepository;
import com.library.library_backend.repository.RezervasyonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RezervasyonService {

    private final RezervasyonRepository rezervasyonRepository;
    private final KullaniciRepository kullaniciRepository;
    private final KitapRepository kitapRepository;

    @Transactional
    public Rezervasyon rezervasyonYap(UUID kullaniciId, UUID kitapId) {
        Kullanici kullanici = kullaniciRepository.findById(kullaniciId)
                .orElseThrow(() -> new KaynakBulunamadiException("Kullanıcı bulunamadı: " + kullaniciId));

        Kitap kitap = kitapRepository.findById(kitapId)
                .orElseThrow(() -> new KaynakBulunamadiException("Kitap bulunamadı: " + kitapId));

        // Stokta kitap varsa rezervasyona gerek yok, doğrudan ödünç almalı
        if (kitap.getMevcutAdet() > 0) {
            throw new GecersizIslemException("Bu kitap şu an rafta mevcuttur. Rezervasyon yerine doğrudan ödünç alabilirsiniz!");
        }

        // Sıradaki numarasını belirle
        long siradakiKisiSayisi = rezervasyonRepository.countByKitapIdAndDurum(kitapId, RezervasyonDurumu.BEKLEMEDE);
        int yeniSiraNo = (int) siradakiKisiSayisi + 1;

        Rezervasyon rezervasyon = Rezervasyon.builder()
                .kullanici(kullanici)
                .kitap(kitap)
                .siraNo(yeniSiraNo)
                .durum(RezervasyonDurumu.BEKLEMEDE)
                .build();

        return rezervasyonRepository.save(rezervasyon);
    }

    public List<Rezervasyon> kullaniciRezervasyonlari(UUID kullaniciId) {
        return rezervasyonRepository.findByKullaniciId(kullaniciId);
    }
}