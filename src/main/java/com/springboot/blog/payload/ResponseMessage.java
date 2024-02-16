package com.springboot.blog.payload;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ResponseMessage {
    private String message;
}
