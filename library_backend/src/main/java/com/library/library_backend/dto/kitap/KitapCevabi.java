package com.library.library_backend.dto.kitap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KitapCevabi {
    private UUID id;
    private String isbn;
    private String baslik;
    private String kategoriAdi;
    private String yazarAdi;
    private String yayineviAdi;
    private Integer yayinYili;
    private Integer toplamAdet;
    private Integer mevcutAdet;
    private String rafKonumu;
}