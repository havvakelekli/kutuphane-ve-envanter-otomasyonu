package com.library.library_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HataCevabi {
    private LocalDateTime zamanDamgasi;
    private int durumKodu;
    private String hata;
    private String mesaj;
    private String yol;
}