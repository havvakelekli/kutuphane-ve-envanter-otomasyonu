package com.library.library_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GecersizIslemException extends RuntimeException {
    public GecersizIslemException(String mesaj) {
        super(mesaj);
    }
}