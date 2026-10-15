package com.library.library_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class KaynakBulunamadiException extends RuntimeException {
    public KaynakBulunamadiException(String mesaj) {
        super(mesaj);
    }
}