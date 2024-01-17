package com.springboot.blog.utils;

import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.exception.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerUtils {
    public static ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, Long id, String entityName) {
        return new ResponseEntity<>(
                new ResponseError("Not found",
                        "Couldn't find any " + entityName + " with the given id: " + id, HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND);
    }
}
