package com.library.library_backend.service;

import com.library.library_backend.dto.auth.AuthCevabi;
import com.library.library_backend.dto.auth.GirisIstegi;
import com.library.library_backend.dto.auth.KayitIstegi;
import com.library.library_backend.entity.Kullanici;
import com.library.library_backend.entity.Rol;
import com.library.library_backend.repository.KullaniciRepository;
import com.library.library_backend.security.JwtService;
import com.library.library_backend.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KullaniciRepository kullaniciRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthCevabi kayitOl(KayitIstegi istek) {
        if (kullaniciRepository.existsByEposta(istek.getEposta())) {
            throw new RuntimeException("Bu e-posta adresi zaten kullanılıyor: " + istek.getEposta());
        }

        Kullanici kullanici = Kullanici.builder()
                .adSoyad(istek.getAdSoyad())
                .eposta(istek.getEposta())
                .sifre(passwordEncoder.encode(istek.getSifre())) // Şifre BCrypt ile hash'lenir
                .telefonNo(istek.getTelefonNo())
                .rol(istek.getRol() != null ? istek.getRol() : Rol.ROL_UYE)
                .aktifMi(true)
                .build();

        Kullanici kaydedilen = kullaniciRepository.save(kullanici);

        UserDetails userDetails = userDetailsService.loadUserByUsername(kaydedilen.getEposta());
        String jwtToken = jwtService.generateToken(userDetails);

        return AuthCevabi.builder()
                .token(jwtToken)
                .id(kaydedilen.getId())
                .adSoyad(kaydedilen.getAdSoyad())
                .eposta(kaydedilen.getEposta())
                .rol(kaydedilen.getRol())
                .build();
    }

    public AuthCevabi girisYap(GirisIstegi istek) {
        // Spring Security ile kullanıcı e-posta ve şifresi doğrulanır
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        istek.getEposta(),
                        istek.getSifre()
                )
        );

        Kullanici kullanici = kullaniciRepository.findByEposta(istek.getEposta())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(kullanici.getEposta());
        String jwtToken = jwtService.generateToken(userDetails);

        return AuthCevabi.builder()
                .token(jwtToken)
                .id(kullanici.getId())
                .adSoyad(kullanici.getAdSoyad())
                .eposta(kullanici.getEposta())
                .rol(kullanici.getRol())
                .build();
    }
}