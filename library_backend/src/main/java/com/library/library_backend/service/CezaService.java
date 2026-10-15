package com.library.library_backend.service;

import com.library.library_backend.entity.CezaOdemesi;
import com.library.library_backend.exception.GecersizIslemException;
import com.library.library_backend.exception.KaynakBulunamadiException;
import com.library.library_backend.repository.CezaOdemesiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CezaService {

    private final CezaOdemesiRepository cezaOdemesiRepository;

    public List<CezaOdemesi> kullaniciCezalari(UUID kullaniciId) {
        return cezaOdemesiRepository.findByKullaniciId(kullaniciId);
    }

    @Transactional
    public CezaOdemesi cezaOde(UUID cezaId) {
        CezaOdemesi ceza = cezaOdemesiRepository.findById(cezaId)
                .orElseThrow(() -> new KaynakBulunamadiException("Ceza kaydı bulunamadı: " + cezaId));

        if (ceza.getOdendiMi()) {
            throw new GecersizIslemException("Bu ceza borcu zaten ödenmiş!");
        }

        ceza.setOdendiMi(true);
        ceza.setOdemeTarihi(LocalDateTime.now());

        return cezaOdemesiRepository.save(ceza);
    }
}