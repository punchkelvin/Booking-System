package com.example.bookingDemo.exceptions;

import com.example.bookingDemo.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<BaseResponse> handleUsernameTaken(UserAlreadyExistsException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BaseResponse(ex.getMessage(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<BaseResponse> handleUsernameNotFound(UsernameNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BaseResponse(ex.getMessage(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse> handleRuntimeException(RuntimeException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BaseResponse(ex.getMessage(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<BaseResponse> handleUserNotFoundException(UserNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BaseResponse(ex.getMessage(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now()));
    }
}
