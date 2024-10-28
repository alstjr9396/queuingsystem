package me.minseok.queueing.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ApplicationAdvice {

    @ExceptionHandler(ApplicationException.class)
    Mono<?> applicationExceptionHanlder(ApplicationException e) {
        return Mono.just(ResponseEntity
                .status(e.getHttpStatus())
                .body(new ServerExceptionResponse(e.getCode(), e.getMessage())));
    }

    public record ServerExceptionResponse(String code, String message) {
    }

}
