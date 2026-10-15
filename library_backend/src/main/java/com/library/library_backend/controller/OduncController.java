package com.library.library_backend.controller;

import com.library.library_backend.dto.odunc.OduncAlmaIstegi;
import com.library.library_backend.dto.odunc.OduncCevabi;
import com.library.library_backend.service.OduncService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/odunc")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OduncController {


    private final com.library.library_backend.service.PdfService pdfService;

    @GetMapping("/fis/{id}")
    public ResponseEntity<org.springframework.core.io.InputStreamResource> oduncFisiIndir(@PathVariable UUID id) {
        java.io.ByteArrayInputStream bis = pdfService.oduncFisiUret(id);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=odunc_fisi_" + id + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(new org.springframework.core.io.InputStreamResource(bis));
    }


    private final OduncService oduncService;

    @PostMapping("/al")
    public ResponseEntity<OduncCevabi> oduncAl(@Valid @RequestBody OduncAlmaIstegi istek) {
        return ResponseEntity.ok(oduncService.oduncAl(istek));
    }

    @PostMapping("/iade/{id}")
    public ResponseEntity<OduncCevabi> iadeEt(@PathVariable UUID id) {
        return ResponseEntity.ok(oduncService.iadeEt(id));
    }

    @GetMapping("/kullanici/{kullaniciId}")
    public ResponseEntity<List<OduncCevabi>> kullanicininOduncleri(@PathVariable UUID kullaniciId) {
        return ResponseEntity.ok(oduncService.kullanicininOduncleri(kullaniciId));
    }
}