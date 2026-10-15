package com.library.library_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "yazarlar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Yazar {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ad_soyad", nullable = false, length = 150)
    private String adSoyad;

    @Column(name = "biyografi", columnDefinition = "TEXT")
    private String biyografi;

    @CreationTimestamp
    @Column(name = "olusturulma_tarihi", updatable = false)
    private LocalDateTime olusturulmaTarihi;
}