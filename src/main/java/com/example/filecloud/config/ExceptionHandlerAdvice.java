package com.example.filecloud.config;

import com.example.filecloud.exception.BadUserException;
import com.example.filecloud.exception.DeleteFileException;
import com.example.filecloud.exception.SaveFileException;
import com.example.filecloud.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(SaveFileException.class)
    public ResponseEntity<ErrorMessage> invalidCredentials(SaveFileException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeleteFileException.class)
    public ResponseEntity<ErrorMessage> invalidCredentials(DeleteFileException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadUserException.class)
    public ResponseEntity<ErrorMessage> invalidCredentials(BadUserException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
