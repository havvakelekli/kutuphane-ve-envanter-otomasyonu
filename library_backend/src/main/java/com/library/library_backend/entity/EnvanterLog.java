package com.library.library_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "envanter_loglari")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvanterLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kitap_id", nullable = false)
    private Kitap kitap;

    @Enumerated(EnumType.STRING)
    @Column(name = "islem_turu", nullable = false, length = 50)
    private EnvanterIslemTuru islemTuru;

    @Column(name = "adet_degisimi", nullable = false)
    private Integer adetDegisimi;

    @Column(name = "aciklama", columnDefinition = "TEXT")
    private String aciklama;

    @CreationTimestamp
    @Column(name = "olusturulma_tarihi", updatable = false)
    private LocalDateTime olusturulmaTarihi;
}