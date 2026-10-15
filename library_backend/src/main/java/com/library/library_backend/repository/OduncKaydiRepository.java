package com.library.library_backend.repository;

import com.library.library_backend.entity.OduncDurumu;
import com.library.library_backend.entity.OduncKaydi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OduncKaydiRepository extends JpaRepository<OduncKaydi, UUID> {
    List<OduncKaydi> findByKullaniciId(UUID kullaniciId);

    // Kullanıcının şu an üzerinde kaç tane teslim etmediği kitap olduğunu sayar (Max 3 limiti için)
    long countByKullaniciIdAndDurum(UUID kullaniciId, OduncDurumu durum);

    // Gece cron job'ının iadesi gecikenleri bulması için
    List<OduncKaydi> findByDurumAndSonTeslimTarihiBefore(OduncDurumu durum, LocalDateTime simdi);
}