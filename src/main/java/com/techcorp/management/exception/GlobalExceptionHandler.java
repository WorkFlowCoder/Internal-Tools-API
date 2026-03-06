package com.techcorp.management.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@io.swagger.v3.oas.annotations.Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

  // 1. Validation échouée (HTTP 400)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> details = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              details.put(fieldName, errorMessage);
            });

    ErrorResponse error =
        ErrorResponse.builder().error("Validation failed").details(details).build();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  // 2. Ressource introuvable (HTTP 404)
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
    ErrorResponse error =
        ErrorResponse.builder().error("Tool not found").message(ex.getMessage()).build();
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  // 3. Mauvaise requête générique (HTTP 400 pour tes BadRequestException)
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
    ErrorResponse error =
        ErrorResponse.builder().error("Bad request").message(ex.getMessage()).build();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  // 4. Erreur serveur (HTTP 500)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
    ErrorResponse error =
        ErrorResponse.builder()
            .error("Internal server error")
            .message(ex.getMessage()) // En prod, évite de mettre ex.getMessage() pour la sécurité
            .build();
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
