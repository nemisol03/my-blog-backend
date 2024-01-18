package com.springboot.blog.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
@Setter
public class ResourceNotFoundException extends Exception {
    private String resourceName;
    private String fieldName;
    private long fieldValue;

 public ResourceNotFoundException(String message) {
     super(message);
 }
}
