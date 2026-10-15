package com.library.library_backend.service;

import com.library.library_backend.dto.odunc.OduncAlmaIstegi;
import com.library.library_backend.dto.odunc.OduncCevabi;
import com.library.library_backend.entity.*;
import com.library.library_backend.exception.AktifCezaBorcuException;
import com.library.library_backend.exception.GecersizIslemException;
import com.library.library_backend.exception.KaynakBulunamadiException;
import com.library.library_backend.exception.YetersizStokException;
import com.library.library_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OduncService {

    private final OduncKaydiRepository oduncKaydiRepository;
    private final KullaniciRepository kullaniciRepository;
    private final KitapRepository kitapRepository;
    private final CezaOdemesiRepository cezaOdemesiRepository;
    private final EnvanterLogRepository envanterLogRepository;

    // GÜNLÜK GECİKME CEZASI: 10 TL
    private static final BigDecimal GUNLUK_CEZA_TUTARI = new BigDecimal("10.00");

    @Transactional
    public OduncCevabi oduncAl(OduncAlmaIstegi istek) {
        Kullanici kullanici = kullaniciRepository.findById(istek.getKullaniciId())
                .orElseThrow(() -> new KaynakBulunamadiException("Kullanıcı bulunamadı: " + istek.getKullaniciId()));

        Kitap kitap = kitapRepository.findById(istek.getKitapId())
                .orElseThrow(() -> new KaynakBulunamadiException("Kitap bulunamadı: " + istek.getKitapId()));

        // 1. KURAL: Ödenmemiş ceza borcu var mı?
        if (cezaOdemesiRepository.existsByKullaniciIdAndOdendiMiFalse(kullanici.getId())) {
            throw new AktifCezaBorcuException("Ödenmemiş ceza borcunuz bulunmaktadır! Ödeme yapmadan yeni kitap alamazsınız.");
        }

        // 2. KURAL: Üzerindeki aktif kitap sayısı maksimum 3 olabilir
        long aktifKitapSayisi = oduncKaydiRepository.countByKullaniciIdAndDurum(kullanici.getId(), OduncDurumu.ODUNC_ALINDI);
        if (aktifKitapSayisi >= 3) {
            throw new GecersizIslemException("Bir üye aynı anda en fazla 3 kitap ödünç alabilir!");
        }

        // 3. KURAL: Rafta stok var mı?
        if (kitap.getMevcutAdet() <= 0) {
            throw new YetersizStokException("Bu kitaptan rafta kalmamıştır! Dilerseniz rezervasyon (sıra) oluşturabilirsiniz.");
        }

        // 4. İŞLEM: Stoktan 1 düş
        kitap.setMevcutAdet(kitap.getMevcutAdet() - 1);
        kitapRepository.save(kitap);

        // 5. İŞLEM: Ödünç kaydı aç
        LocalDateTime simdi = LocalDateTime.now();
        LocalDateTime sonTeslim = simdi.plusDays(istek.getGunSayisi());

        OduncKaydi kayit = OduncKaydi.builder()
                .kullanici(kullanici)
                .kitap(kitap)
                .oduncTarihi(simdi)
                .sonTeslimTarihi(sonTeslim)
                .durum(OduncDurumu.ODUNC_ALINDI)
                .build();

        OduncKaydi kaydedilen = oduncKaydiRepository.save(kayit);

        // 6. İŞLEM: Envanter Logu
        EnvanterLog log = EnvanterLog.builder()
                .kitap(kitap)
                .islemTuru(EnvanterIslemTuru.CIKARILDI)
                .adetDegisimi(-1)
                .aciklama("Kitap ödünç verildi. Kullanıcı: " + kullanici.getAdSoyad())
                .build();
        envanterLogRepository.save(log);

        return entityToCevap(kaydedilen);
    }

    @Transactional
    public OduncCevabi iadeEt(UUID oduncKayitId) {
        OduncKaydi kayit = oduncKaydiRepository.findById(oduncKayitId)
                .orElseThrow(() -> new KaynakBulunamadiException("Ödünç kaydı bulunamadı: " + oduncKayitId));

        if (kayit.getDurum() == OduncDurumu.IADE_EDILDI) {
            throw new GecersizIslemException("Bu kitap zaten daha önce iade edilmiş!");
        }

        LocalDateTime simdi = LocalDateTime.now();
        kayit.setIadeTarihi(simdi);
        kayit.setDurum(OduncDurumu.IADE_EDILDI);

        // 1. İŞLEM: Raftaki stok sayısını +1 artır
        Kitap kitap = kayit.getKitap();
        kitap.setMevcutAdet(kitap.getMevcutAdet() + 1);
        kitapRepository.save(kitap);

        // 2. İŞLEM: Gecikme Cezası Hesaplama (Teslim tarihi geçmiş mi?)
        if (simdi.isAfter(kayit.getSonTeslimTarihi())) {
            long gecikenGun = Duration.between(kayit.getSonTeslimTarihi(), simdi).toDays();
            if (gecikenGun == 0) gecikenGun = 1; // Aynı gün içinde birkaç saat geçse bile 1 gün say

            BigDecimal cezaTutari = GUNLUK_CEZA_TUTARI.multiply(BigDecimal.valueOf(gecikenGun));

            CezaOdemesi ceza = CezaOdemesi.builder()
                    .oduncKaydi(kayit)
                    .kullanici(kayit.getKullanici())
                    .tutar(cezaTutari)
                    .odendiMi(false)
                    .build();
            cezaOdemesiRepository.save(ceza);
        }

        // 3. İŞLEM: Envanter Logu
        EnvanterLog log = EnvanterLog.builder()
                .kitap(kitap)
                .islemTuru(EnvanterIslemTuru.EKLENDI)
                .adetDegisimi(1)
                .aciklama("Kitap iade alındı. Kullanıcı: " + kayit.getKullanici().getAdSoyad())
                .build();
        envanterLogRepository.save(log);

        return entityToCevap(oduncKaydiRepository.save(kayit));
    }

    public List<OduncCevabi> kullanicininOduncleri(UUID kullaniciId) {
        return oduncKaydiRepository.findByKullaniciId(kullaniciId).stream()
                .map(this::entityToCevap)
                .collect(Collectors.toList());
    }

    private OduncCevabi entityToCevap(OduncKaydi kayit) {
        long kalanGun = 0;
        if (kayit.getDurum() == OduncDurumu.ODUNC_ALINDI) {
            kalanGun = Duration.between(LocalDateTime.now(), kayit.getSonTeslimTarihi()).toDays();
        }

        return OduncCevabi.builder()
                .id(kayit.getId())
                .kullaniciId(kayit.getKullanici().getId())
                .kullaniciAdi(kayit.getKullanici().getAdSoyad())
                .kitapId(kayit.getKitap().getId())
                .kitapBaslik(kayit.getKitap().getBaslik())
                .isbn(kayit.getKitap().getIsbn())
                .oduncTarihi(kayit.getOduncTarihi())
                .sonTeslimTarihi(kayit.getSonTeslimTarihi())
                .iadeTarihi(kayit.getIadeTarihi())
                .durum(kayit.getDurum())
                .kalanGun(kalanGun)
                .build();
    }
}