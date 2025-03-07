package com.yj.flooringmastery.service;

public class NoSuchOrderException extends RuntimeException {
    public NoSuchOrderException(String message) {
        super(message);
    }
  public NoSuchOrderException(String message, Throwable cause) {
    super(message, cause);
  }
}
