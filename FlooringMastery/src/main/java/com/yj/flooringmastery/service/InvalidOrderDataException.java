package com.yj.flooringmastery.service;

public class InvalidOrderDataException extends RuntimeException {
    public InvalidOrderDataException(String message) {
        super(message);
    }
    public InvalidOrderDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
