package com.library.library_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "odunc_kayitlari")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OduncKaydi {

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
    @Column(name = "odunc_tarihi", updatable = false)
    private LocalDateTime oduncTarihi;

    @Column(name = "son_teslim_tarihi", nullable = false)
    private LocalDateTime sonTeslimTarihi;

    @Column(name = "iade_tarihi")
    private LocalDateTime iadeTarihi;

    @Enumerated(EnumType.STRING)
    @Column(name = "durum", nullable = false, length = 30)
    @Builder.Default
    private OduncDurumu durum = OduncDurumu.ODUNC_ALINDI;

    @CreationTimestamp
    @Column(name = "olusturulma_tarihi", updatable = false)
    private LocalDateTime olusturulmaTarihi;
}