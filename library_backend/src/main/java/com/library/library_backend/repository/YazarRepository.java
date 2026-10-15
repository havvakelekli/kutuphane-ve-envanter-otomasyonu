package com.library.library_backend.repository;

import com.library.library_backend.entity.Yazar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface YazarRepository extends JpaRepository<Yazar, UUID> {
}