package com.library.library_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "kitaplar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Kitap {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(name = "baslik", nullable = false, length = 200)
    private String baslik;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kategori_id")
    private Kategori kategori;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "yazar_id")
    private Yazar yazar;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "yayinevi_id")
    private Yayinevi yayinevi;

    @Column(name = "yayin_yili")
    private Integer yayinYili;

    @Column(name = "toplam_adet", nullable = false)
    @Builder.Default
    private Integer toplamAdet = 1;

    @Column(name = "mevcut_adet", nullable = false)
    @Builder.Default
    private Integer mevcutAdet = 1;

    @Column(name = "raf_konumu", length = 50)
    private String rafKonumu;

    @CreationTimestamp
    @Column(name = "olusturulma_tarihi", updatable = false)
    private LocalDateTime olusturulmaTarihi;

    @UpdateTimestamp
    @Column(name = "guncellenme_tarihi")
    private LocalDateTime guncellenmeTarihi;
}