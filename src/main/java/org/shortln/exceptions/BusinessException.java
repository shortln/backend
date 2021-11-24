package org.shortln.exceptions;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private HttpStatus status;
    private String message;
    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
    public int getStatusCode() {
        return this.status.value();
    }
}
