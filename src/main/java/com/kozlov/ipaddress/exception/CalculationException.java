package com.kozlov.ipaddress.exception;

public class CalculationException extends RuntimeException {

    public CalculationException(String message, Throwable e) {
        super(message, e);
    }

}
