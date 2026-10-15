package com.library.library_backend.dto.auth;

import com.library.library_backend.entity.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class KayitIstegi {

    @NotBlank(message = "Ad Soyad alanı boş bırakılamaz")
    private String adSoyad;

    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @NotBlank(message = "E-posta alanı boş bırakılamaz")
    private String eposta;

    @NotBlank(message = "Şifre alanı boş bırakılamaz")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır")
    private String sifre;

    private String telefonNo;

    private Rol rol; // Belirtilmezse varsayılan ROL_UYE atanır
}