package org.shreejalarammandir.service;

public class NotDeletableException extends RuntimeException {

    public NotDeletableException(String message) {
        super(message);
    }
}
