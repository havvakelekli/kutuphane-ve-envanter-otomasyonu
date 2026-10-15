package com.library.library_backend.repository;

import com.library.library_backend.entity.EnvanterLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EnvanterLogRepository extends JpaRepository<EnvanterLog, UUID> {
    List<EnvanterLog> findByKitapId(UUID kitapId);
}