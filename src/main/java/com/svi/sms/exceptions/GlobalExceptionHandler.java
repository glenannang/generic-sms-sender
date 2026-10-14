package com.svi.sms.exceptions;

import com.svi.sms.dto.response.SmsResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SmsSendingException.class)
    public ResponseEntity<SmsResponseDTO> handleSmsSendingException() {
        SmsResponseDTO response = new SmsResponseDTO(
                false,
                "Unable to send SMS at this time."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SmsResponseDTO> handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("Request validation failed.");

        return ResponseEntity.badRequest().body(new SmsResponseDTO(false, message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<SmsResponseDTO> handleUnreadableMessageException() {
        SmsResponseDTO response = new SmsResponseDTO(
                false,
                "Request body is invalid or malformed."
        );

        return ResponseEntity.badRequest().body(response);
    }
}
