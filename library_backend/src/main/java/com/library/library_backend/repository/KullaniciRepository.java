package com.library.library_backend.repository;

import com.library.library_backend.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KullaniciRepository extends JpaRepository<Kullanici, UUID> {

    // E-posta adresine göre kullanıcıyı veritabanında arar
    Optional<Kullanici> findByEposta(String eposta);

    // Kayıt olurken bu e-posta zaten var mı kontrol eder
    boolean existsByEposta(String eposta);
}