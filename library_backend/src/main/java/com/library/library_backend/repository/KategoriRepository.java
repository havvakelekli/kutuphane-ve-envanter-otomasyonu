package com.library.library_backend.repository;

import com.library.library_backend.entity.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KategoriRepository extends JpaRepository<Kategori, UUID> {
    Optional<Kategori> findByAd(String ad);
    boolean existsByAd(String ad);
}