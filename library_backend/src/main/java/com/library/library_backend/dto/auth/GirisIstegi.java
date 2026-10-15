package com.library.library_backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GirisIstegi {

    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @NotBlank(message = "E-posta alanı boş bırakılamaz")
    private String eposta;

    @NotBlank(message = "Şifre alanı boş bırakılamaz")
    private String sifre;
}