package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    @JsonProperty("page_no")
    private int pageNo;
    @JsonProperty("page_size")
    private int pageSize;
    private List<PostDTO> content;
    @JsonProperty("total_elements")
    private long totalElements;
    @JsonProperty("total_page")
    private int totalPage;
    private boolean last;
}
