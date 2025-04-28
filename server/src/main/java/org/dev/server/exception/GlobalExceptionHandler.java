package org.dev.server.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    //@Valid by default arunca eroarea 500, eu am suprascris sa arunce 400, deoarece 500 e eroare de server, astfel in controller
    //daca nu e valid aruncam 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                err -> errors.put(err.getField(), err.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }
    //email existent -> bad request
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Email address already exists.");
        return ResponseEntity.badRequest().body(errors);
    }

    //username existent -> bad request
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Username already exists.");
        return ResponseEntity.badRequest().body(errors);
    }

    //asset existent
    @ExceptionHandler(AssetAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleAssetAlreadyExistsException(AssetAlreadyExistsException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    //asset inexistent
    @ExceptionHandler(AssetNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAssetNotFoundException(AssetNotFoundException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    //jdbc errors
    @ExceptionHandler(JdbcException.class)
    public ResponseEntity<Map<String, String>> handleJdbcException(JdbcException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }
    //else exception for jdbc possible thrown exceptions
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<Map<String, String>> handleDatabaseException(DatabaseException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());

        return ResponseEntity.internalServerError().body(errors);
    }

    @ExceptionHandler(InsufficientQuantityException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientQuantityException(InsufficientQuantityException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }
}
