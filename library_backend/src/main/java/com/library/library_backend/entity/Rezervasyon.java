package com.library.library_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rezervasyonlar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rezervasyon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kitap_id", nullable = false)
    private Kitap kitap;

    @CreationTimestamp
    @Column(name = "rezervasyon_tarihi", updatable = false)
    private LocalDateTime rezervasyonTarihi;

    @Column(name = "sira_no", nullable = false)
    @Builder.Default
    private Integer siraNo = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "durum", nullable = false, length = 30)
    @Builder.Default
    private RezervasyonDurumu durum = RezervasyonDurumu.BEKLEMEDE;

    @CreationTimestamp
    @Column(name = "olusturulma_tarihi", updatable = false)
    private LocalDateTime olusturulmaTarihi;
}