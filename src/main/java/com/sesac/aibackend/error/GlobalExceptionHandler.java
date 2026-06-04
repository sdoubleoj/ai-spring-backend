package com.sesac.aibackend.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // @ExceptionHandler가 붙었으니, NotFoundException가 발생하면(들어오면), handleNotFound 메서드 실행
    // NotFoundException(Not Found Error)는 자원이 없어서 나는 404 에러
    // 자원이 없는건, @GetMapping("/{id}") & @PutMapping("/{id}") & @DeleteMapping("/{id}")
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of("NOT_FOUND", e.getMessage()));
    }
}
