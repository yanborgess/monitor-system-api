package com.yan.security.monitorSystem.controllers.exeptions;


import com.yan.security.monitorSystem.controllers.dtos.ValidationErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice // Avisa que esta classe trata erros de todos os controllers
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // Identifica o erro de validação
    public ResponseEntity<List<ValidationErrorDTO>> handleValidationErrors(MethodArgumentNotValidException ex) {

        // Pegamos todos os erros que o Spring encontrou e transformamos no nosso DTO simples
        List<ValidationErrorDTO> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ValidationErrorDTO(err.getField(), err.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(RuntimeException.class) // Trata o erro que lançamos no Service
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}