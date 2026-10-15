package com.library.library_backend.dto.auth;

import com.library.library_backend.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthCevabi {
    private String token;
    private UUID id;
    private String adSoyad;
    private String eposta;
    private Rol rol;
}