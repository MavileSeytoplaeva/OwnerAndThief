package org.example;

public class NoStorageLeftException extends RuntimeException {
    public NoStorageLeftException(String message) {
        super(message);
    }
}
