package com.library.library_backend.dto.odunc;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class OduncAlmaIstegi {

    @NotNull(message = "Kullanıcı ID zorunludur")
    private UUID kullaniciId;

    @NotNull(message = "Kitap ID zorunludur")
    private UUID kitapId;

    @Min(value = 1, message = "En az 1 gün ödünç alınabilir")
    @Max(value = 30, message = "En fazla 30 gün ödünç alınabilir")
    private int gunSayisi = 15; // Varsayılan 15 gün
}