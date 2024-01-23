package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String message;

    @JsonIgnore
    @JsonProperty("parent_id")
    private Long parentId;
    @JsonIgnoreProperties({"email","address"})
    private UserDTO user;

    @JsonProperty("post_id")
    @JsonIdentityReference
    private Long postId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CommentDTO> children;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("created_date")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("modified_date")
    private LocalDateTime modifiedDate;

}
