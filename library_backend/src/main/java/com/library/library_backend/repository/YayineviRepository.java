package com.library.library_backend.repository;

import com.library.library_backend.entity.Yayinevi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface YayineviRepository extends JpaRepository<Yayinevi, UUID> {
    boolean existsByAd(String ad);
}