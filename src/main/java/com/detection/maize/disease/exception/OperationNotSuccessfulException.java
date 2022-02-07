package com.detection.maize.disease.exception;

public class OperationNotSuccessfulException extends RuntimeException{
    public OperationNotSuccessfulException(String message) {
        super(message);
    }

    public OperationNotSuccessfulException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperationNotSuccessfulException(Throwable cause) {
        super(cause);
    }

    protected OperationNotSuccessfulException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
