package com.library.library_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "yayinevleri")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Yayinevi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ad", nullable = false, unique = true, length = 150)
    private String ad;

    @Column(name = "adres", columnDefinition = "TEXT")
    private String adres;

    @Column(name = "iletisim_telefonu", length = 20)
    private String iletisimTelefonu;

    @CreationTimestamp
    @Column(name = "olusturulma_tarihi", updatable = false)
    private LocalDateTime olusturulmaTarihi;
}