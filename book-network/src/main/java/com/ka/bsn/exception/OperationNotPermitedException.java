package com.ka.bsn.exception;

public class OperationNotPermitedException extends RuntimeException {
    public OperationNotPermitedException(String message) {
        super(message);
    }
}
