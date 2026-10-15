package com.library.library_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "kullanicilar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Kullanici {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ad_soyad", nullable = false, length = 100)
    private String adSoyad;

    @Column(name = "eposta", nullable = false, unique = true, length = 100)
    private String eposta;

    @Column(name = "sifre", nullable = false)
    private String sifre;

    @Column(name = "telefon_no", length = 20)
    private String telefonNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 30)
    private Rol rol;

    @Column(name = "aktif_mi")
    @Builder.Default
    private Boolean aktifMi = true;

    @CreationTimestamp
    @Column(name = "olusturulma_tarihi", updatable = false)
    private LocalDateTime olusturulmaTarihi;

    @UpdateTimestamp
    @Column(name = "guncellenme_tarihi")
    private LocalDateTime guncellenmeTarihi;
}