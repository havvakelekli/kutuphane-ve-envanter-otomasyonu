package com.library.library_backend.security;

import com.library.library_backend.entity.Kullanici;
import com.library.library_backend.repository.KullaniciRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final KullaniciRepository kullaniciRepository;

    @Override
    public UserDetails loadUserByUsername(String eposta) throws UsernameNotFoundException {
        Kullanici kullanici = kullaniciRepository.findByEposta(eposta)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + eposta));

        // Kullanıcının rolünü (Örn: ROL_ADMIN) Spring Security yetkisine çeviriyoruz
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(kullanici.getRol().name());

        return new User(
                kullanici.getEposta(),
                kullanici.getSifre(),
                kullanici.getAktifMi(),
                true,
                true,
                true,
                Collections.singletonList(authority)
        );
    }
}