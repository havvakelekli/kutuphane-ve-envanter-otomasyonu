package com.library.library_backend.repository;

import com.library.library_backend.entity.Kitap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface KitapRepository extends JpaRepository<Kitap, UUID> {
    Optional<Kitap> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);

    // Başlığa veya Yazar adına göre arama metodu
    @Query("SELECT k FROM Kitap k WHERE LOWER(k.baslik) LIKE LOWER(CONCAT('%', :kelime, '%')) OR LOWER(k.yazar.adSoyad) LIKE LOWER(CONCAT('%', :kelime, '%'))")
    List<Kitap> kitapAra(@Param("kelime") String kelime);
}