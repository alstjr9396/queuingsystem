package me.minseok.queueing.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApplicationException extends RuntimeException{

    private HttpStatus httpStatus;

    private String code;

    private String message;
}
