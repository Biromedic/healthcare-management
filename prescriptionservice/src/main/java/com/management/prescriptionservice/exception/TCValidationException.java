package com.management.prescriptionservice.exception;

public class TCValidationException extends RuntimeException {
    public TCValidationException(String message) {
        super(message);
    }
}