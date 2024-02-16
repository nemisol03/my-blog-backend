package com.springboot.blog.exception;

public class UniqueFieldViolationException extends Exception{

    public UniqueFieldViolationException(String message) {
        super(message);

    }
}
