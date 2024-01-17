package com.springboot.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public class ResponseError {
    private Date date;
    private String name;
    private String message;
    private int status;

    public ResponseError(String name,String message,int status) {
        date = new Date();
        this.name = name;
        this.message = message;
        this.status = status;
    }
}
