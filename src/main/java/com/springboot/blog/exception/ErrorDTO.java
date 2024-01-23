package com.springboot.blog.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ErrorDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")

    private Date timestamp;
    private String path;
    private int status;
    @JsonProperty("errors")
    private List<String> errorList = new ArrayList<>();

    public void addError(String error) {
        errorList.add(error);
    }


}
