package com.springboot.blog.exception;

public class ResourceInUseException extends Exception {
    private String message;

    public ResourceInUseException(String message) {
        super(message);
    }
}
