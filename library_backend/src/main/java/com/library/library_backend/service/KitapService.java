package com.library.library_backend.service;

import com.library.library_backend.dto.kitap.KitapCevabi;
import com.library.library_backend.dto.kitap.KitapIstegi;
import com.library.library_backend.entity.*;
import com.library.library_backend.exception.GecersizIslemException;
import com.library.library_backend.exception.KaynakBulunamadiException;
import com.library.library_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KitapService {

    private final KitapRepository kitapRepository;
    private final KategoriRepository kategoriRepository;
    private final YazarRepository yazarRepository;
    private final YayineviRepository yayineviRepository;
    private final EnvanterLogRepository envanterLogRepository;

    @Transactional
    public KitapCevabi kitapEkle(KitapIstegi istek) {
        if (kitapRepository.existsByIsbn(istek.getIsbn())) {
            throw new GecersizIslemException("Bu ISBN numarasına sahip kitap zaten mevcut: " + istek.getIsbn());
        }

        Kategori kategori = istek.getKategoriId() != null
                ? kategoriRepository.findById(istek.getKategoriId()).orElse(null) : null;
        Yazar yazar = istek.getYazarId() != null
                ? yazarRepository.findById(istek.getYazarId()).orElse(null) : null;
        Yayinevi yayinevi = istek.getYayineviId() != null
                ? yayineviRepository.findById(istek.getYayineviId()).orElse(null) : null;

        Kitap kitap = Kitap.builder()
                .isbn(istek.getIsbn())
                .baslik(istek.getBaslik())
                .kategori(kategori)
                .yazar(yazar)
                .yayinevi(yayinevi)
                .yayinYili(istek.getYayinYili())
                .toplamAdet(istek.getToplamAdet())
                .mevcutAdet(istek.getToplamAdet()) // İlk eklendiğinde tümü raftadır
                .rafKonumu(istek.getRafKonumu())
                .build();

        Kitap kaydedilen = kitapRepository.save(kitap);

        // Envanter Denetim Logu (Audit)
        EnvanterLog log = EnvanterLog.builder()
                .kitap(kaydedilen)
                .islemTuru(EnvanterIslemTuru.EKLENDI)
                .adetDegisimi(kaydedilen.getToplamAdet())
                .aciklama("Sisteme yeni kitap girişi yapıldı.")
                .build();
        envanterLogRepository.save(log);

        return entityToCevap(kaydedilen);
    }

    public List<KitapCevabi> tumKitaplar() {
        return kitapRepository.findAll().stream()
                .map(this::entityToCevap)
                .collect(Collectors.toList());
    }

    public List<KitapCevabi> kitapAra(String kelime) {
        return kitapRepository.kitapAra(kelime).stream()
                .map(this::entityToCevap)
                .collect(Collectors.toList());
    }

    public KitapCevabi kitapGetir(UUID id) {
        Kitap kitap = kitapRepository.findById(id)
                .orElseThrow(() -> new KaynakBulunamadiException("Kitap bulunamadı: " + id));
        return entityToCevap(kitap);
    }

    private KitapCevabi entityToCevap(Kitap kitap) {
        return KitapCevabi.builder()
                .id(kitap.getId())
                .isbn(kitap.getIsbn())
                .baslik(kitap.getBaslik())
                .kategoriAdi(kitap.getKategori() != null ? kitap.getKategori().getAd() : "Belirtilmemiş")
                .yazarAdi(kitap.getYazar() != null ? kitap.getYazar().getAdSoyad() : "Belirtilmemiş")
                .yayineviAdi(kitap.getYayinevi() != null ? kitap.getYayinevi().getAd() : "Belirtilmemiş")
                .yayinYili(kitap.getYayinYili())
                .toplamAdet(kitap.getToplamAdet())
                .mevcutAdet(kitap.getMevcutAdet())
                .rafKonumu(kitap.getRafKonumu())
                .build();
    }
}