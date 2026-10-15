package com.library.library_backend.config;

import com.library.library_backend.entity.*;
import com.library.library_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final KullaniciRepository kullaniciRepository;
    private final KategoriRepository kategoriRepository;
    private final YazarRepository yazarRepository;
    private final YayineviRepository yayineviRepository;
    private final KitapRepository kitapRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Eğer kitaplar tablosu boşsa tüm kitapları ve yazarları otomatik yükle
        if (kitapRepository.count() == 0) {
            log.info("Canlı Kütüphane Kitapları Veritabanına Yükleniyor...");

            // 1. Kategoriler
            Kategori roman = kategoriRepository.save(Kategori.builder().ad("Dünya Klasikleri").aciklama("Tarihe mal olmuş başyapıtlar").build());
            Kategori bilim = kategoriRepository.save(Kategori.builder().ad("Bilim Kurgu & Felsefe").aciklama("Gelecek ve insan doğası").build());
            Kategori tarih = kategoriRepository.save(Kategori.builder().ad("Tarih & Araştırma").aciklama("Milli mücadele ve dünya tarihi").build());

            // 2. Yazarlar
            Yazar yasar = yazarRepository.save(Yazar.builder().adSoyad("Yaşar Kemal").biyografi("Usta yazar").build());
            Yazar orwell = yazarRepository.save(Yazar.builder().adSoyad("George Orwell").biyografi("İngiliz yazar").build());
            Yazar dostoyevski = yazarRepository.save(Yazar.builder().adSoyad("Fyodor Dostoyevski").biyografi("Rus yazar").build());
            Yazar ataturk = yazarRepository.save(Yazar.builder().adSoyad("Mustafa Kemal Atatürk").biyografi("Gazi Mustafa Kemal").build());

            // 3. Yayınevleri
            Yayinevi yapiKredi = yayineviRepository.save(Yayinevi.builder().ad("Yapı Kredi Yayınları").adres("İstanbul").build());
            Yayinevi canYayin = yayineviRepository.save(Yayinevi.builder().ad("Can Yayınları").adres("İstanbul").build());
            Yayinevi iletisim = yayineviRepository.save(Yayinevi.builder().ad("İletişim Yayıncılık").adres("İstanbul").build());

            // 4. Kitaplarımız (Yazdığımız gerçek kitaplar)
            kitapRepository.save(Kitap.builder().isbn("9789750719387").baslik("İnce Memed 1").kategori(roman).yazar(yasar).yayinevi(yapiKredi).yayinYili(2020).toplamAdet(8).mevcutAdet(8).rafKonumu("A-101").build());
            kitapRepository.save(Kitap.builder().isbn("9789750718533").baslik("1984").kategori(bilim).yazar(orwell).yayinevi(canYayin).yayinYili(2021).toplamAdet(10).mevcutAdet(10).rafKonumu("B-201").build());
            kitapRepository.save(Kitap.builder().isbn("9789750719394").baslik("Hayvan Çiftliği").kategori(bilim).yazar(orwell).yayinevi(canYayin).yayinYili(2019).toplamAdet(6).mevcutAdet(6).rafKonumu("B-202").build());
            kitapRepository.save(Kitap.builder().isbn("9789750500000").baslik("Suç ve Ceza").kategori(roman).yazar(dostoyevski).yayinevi(iletisim).yayinYili(2022).toplamAdet(5).mevcutAdet(5).rafKonumu("A-102").build());
            kitapRepository.save(Kitap.builder().isbn("9789751600000").baslik("Nutuk").kategori(tarih).yazar(ataturk).yayinevi(yapiKredi).yayinYili(2023).toplamAdet(12).mevcutAdet(12).rafKonumu("T-301").build());

            log.info("Tüm Kitaplar Başarıyla Yüklendi!");
        }
    }
}