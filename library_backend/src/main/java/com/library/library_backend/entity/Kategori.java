package com.library.library_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "kategoriler")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ad", nullable = false, unique = true, length = 100)
    private String ad;

    @Column(name = "aciklama", columnDefinition = "TEXT")
    private String aciklama;

    @CreationTimestamp
    @Column(name = "olusturulma_tarihi", updatable = false)
    private LocalDateTime olusturulmaTarihi;
}