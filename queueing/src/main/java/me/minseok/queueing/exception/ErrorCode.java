package me.minseok.queueing.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ErrorCode {

    QUEUE_ALREADY_REGISTER_MEMBER(HttpStatus.CONFLICT, "MQ-0001", "Already registered in queue");

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;

    public ApplicationException build() {
        return new ApplicationException(httpStatus, code, message);
    }

    public ApplicationException build(Object... args) {
        return new ApplicationException(httpStatus, code, message.formatted(args));
    }

}