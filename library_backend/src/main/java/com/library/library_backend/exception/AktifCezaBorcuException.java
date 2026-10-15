package com.library.library_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AktifCezaBorcuException extends RuntimeException {
    public AktifCezaBorcuException(String mesaj) {
        super(mesaj);
    }
}