package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailTemplateDTO {
 @JsonProperty("CUSTOMER_VERIFY_ACC_SUBJECT")
    private String subject;
    @JsonProperty("CUSTOMER_VERIFY_ACC_CONTENT")
    private String content;


}
