package com.library.library_backend.repository;

import com.library.library_backend.entity.Rezervasyon;
import com.library.library_backend.entity.RezervasyonDurumu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RezervasyonRepository extends JpaRepository<Rezervasyon, UUID> {
    List<Rezervasyon> findByKullaniciId(UUID kullaniciId);

    // Kitap için sıradaki ilk bekleyen rezervasyonu getirir
    Optional<Rezervasyon> findFirstByKitapIdAndDurumOrderBySiraNoAsc(UUID kitapId, RezervasyonDurumu durum);

    // Kitap için kaç kişi sırada bekliyor sayar
    long countByKitapIdAndDurum(UUID kitapId, RezervasyonDurumu durum);
}