package com.library.library_backend.dto.odunc;

import com.library.library_backend.entity.OduncDurumu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OduncCevabi {
    private UUID id;
    private UUID kullaniciId;
    private String kullaniciAdi;
    private UUID kitapId;
    private String kitapBaslik;
    private String isbn;
    private LocalDateTime oduncTarihi;
    private LocalDateTime sonTeslimTarihi;
    private LocalDateTime iadeTarihi;
    private OduncDurumu durum;
    private long kalanGun; // Frontend'deki geri sayım sayacı için
}