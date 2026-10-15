package com.library.library_backend.repository;

import com.library.library_backend.entity.CezaOdemesi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CezaOdemesiRepository extends JpaRepository<CezaOdemesi, UUID> {
    List<CezaOdemesi> findByKullaniciId(UUID kullaniciId);

    // Kullanıcının ödenmemiş ceza borcu var mı kontrolü
    boolean existsByKullaniciIdAndOdendiMiFalse(UUID kullaniciId);
}