package fr.formationacademy.scpiinvestplusapi.globalExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ResponseTemplate> handleGlobalException(GlobalException e, HttpServletRequest request) {
        ResponseTemplate response = ResponseTemplate
                .builder()
                .status(e.getHttpStatus())
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
        log.error("Global Exception", e);
        return  ResponseEntity.status(e.getHttpStatus()).body(response);
    }
}
