package com.springboot.blog.payload;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private int pageNo;
    private int pageSize;
    private List<PostDTO> content;
    private long totalElements;
    private int totalPage;
    private boolean last;
}
