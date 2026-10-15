package com.library.library_backend.dto.kitap;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class KitapIstegi {

    @NotBlank(message = "ISBN alanı zorunludur")
    private String isbn;

    @NotBlank(message = "Kitap başlığı zorunludur")
    private String baslik;

    private UUID kategoriId;
    private UUID yazarId;
    private UUID yayineviId;

    private Integer yayinYili;

    @NotNull(message = "Toplam adet belirtilmelidir")
    @Min(value = 1, message = "En az 1 adet olmalıdır")
    private Integer toplamAdet;

    private String rafKonumu;
}