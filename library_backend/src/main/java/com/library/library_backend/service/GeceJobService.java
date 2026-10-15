package com.library.library_backend.service;

import com.library.library_backend.entity.CezaOdemesi;
import com.library.library_backend.entity.OduncDurumu;
import com.library.library_backend.entity.OduncKaydi;
import com.library.library_backend.repository.CezaOdemesiRepository;
import com.library.library_backend.repository.OduncKaydiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeceJobService {

    private final OduncKaydiRepository oduncKaydiRepository;
    private final CezaOdemesiRepository cezaOdemesiRepository;

    private static final BigDecimal GUNLUK_CEZA_TUTARI = new BigDecimal("10.00");

    // Her gece 00:00:00'da otomatik olarak tetiklenir (Cron: Saniye Dakika Saat Gün Ay HaftaGünü)
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void gecikenKitaplariVeCezalariGuncelle() {
        log.info("Gece Cron Job başladı: İadesi geciken kitaplar taranıyor...");

        LocalDateTime simdi = LocalDateTime.now();
        List<OduncKaydi> gecikenKayitlar = oduncKaydiRepository
                .findByDurumAndSonTeslimTarihiBefore(OduncDurumu.ODUNC_ALINDI, simdi);

        for (OduncKaydi kayit : gecikenKayitlar) {
            kayit.setDurum(OduncDurumu.GECIKTI);
            oduncKaydiRepository.save(kayit);

            long gecikenGun = Duration.between(kayit.getSonTeslimTarihi(), simdi).toDays();
            if (gecikenGun <= 0) gecikenGun = 1;

            BigDecimal toplamCeza = GUNLUK_CEZA_TUTARI.multiply(BigDecimal.valueOf(gecikenGun));

            // Ceza kaydı oluştur
            CezaOdemesi ceza = CezaOdemesi.builder()
                    .oduncKaydi(kayit)
                    .kullanici(kayit.getKullanici())
                    .tutar(toplamCeza)
                    .odendiMi(false)
                    .build();
            cezaOdemesiRepository.save(ceza);

            log.warn("Ceza yazıldı! Kullanıcı: {}, Kitap: {}, Tutar: {} TL",
                    kayit.getKullanici().getAdSoyad(),
                    kayit.getKitap().getBaslik(),
                    toplamCeza);
        }

        log.info("Gece Cron Job tamamlandı. Toplam {} adet gecikmiş kitap güncellendi.", gecikenKayitlar.size());
    }
}