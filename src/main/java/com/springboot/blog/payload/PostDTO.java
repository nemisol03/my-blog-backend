package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String description;
    private String content;
    @JsonIgnoreProperties({"email","address"})
    private UserDTO user;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CommentDTO> comments;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("created_date")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("modified_date")
    private LocalDateTime modifiedDate;


}
