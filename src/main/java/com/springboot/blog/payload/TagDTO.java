package com.springboot.blog.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagDTO {
    private Long id;
    @Size(min = 2,message = "tag's name must be at least 2 characters")
    @NotBlank(message = "tag's name can't be black")
    private String name;
    private String description;
}
