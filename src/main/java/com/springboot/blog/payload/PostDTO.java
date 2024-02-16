package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String slug;
    private String content;

    private String thumbnail;

    @JsonProperty("user_id")
    private Long userId;

    private boolean hot;

    @JsonProperty("tag_id")
     private Long tagId;

    private String status;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;


}
