package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailSettingDTO {
 @JsonProperty("MAIL_HOST")
   private String host;
  @JsonProperty("MAIL_PORT")
    private int port;
    @JsonProperty("MAIL_USERNAME")
    private String username;
    @JsonProperty("MAIL_PASSWORD")
    private String password;
    @JsonProperty("MAIL_SENDER_NAME")
    private String senderName;
    @JsonProperty("MAIL_AUTH")
    private String smtpAuth;
    @JsonProperty("MAIL_SECURED")
    private String smtpSecured;
    @JsonProperty("MAIL_FROM")
    private String mailFrom;

}
