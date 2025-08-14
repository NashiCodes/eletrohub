package com.jp.eletrohub.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;

public class ResponseException extends RuntimeException {
    public ResponseException(Exception e) {
        super(streamLineMessage(e));
    }

    private static String Message(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder("Constraint Violation: ");
        e.getConstraintViolations().forEach(violation -> message.append(violation.getPropertyPath())
                .append(" - ")
                .append(violation.getMessageTemplate())
                .append("; "));
        return message.toString();
    }

    private static String streamLineMessage(Throwable t) {
        var originalMessage = t.getMessage();
        while ((t != null) && !(t instanceof ConstraintViolationException)) {
            t = t.getCause();
        }
        return t == null ? originalMessage
                : Message((ConstraintViolationException) t);
    }

    public ResponseEntity<?> toResponseEntity() {
        return ResponseEntity.badRequest().body(getMessage());
    }
}
