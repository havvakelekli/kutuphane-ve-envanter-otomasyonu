package com.library.library_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ceza_odemeleri")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CezaOdemesi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "odunc_kayit_id")
    private OduncKaydi oduncKaydi;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;

    @Column(name = "tutar", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal tutar = BigDecimal.ZERO;

    @Column(name = "odendi_mi")
    @Builder.Default
    private Boolean odendiMi = false;

    @Column(name = "odeme_tarihi")
    private LocalDateTime odemeTarihi;

    @CreationTimestamp
    @Column(name = "olusturulma_tarihi", updatable = false)
    private LocalDateTime olusturulmaTarihi;
}