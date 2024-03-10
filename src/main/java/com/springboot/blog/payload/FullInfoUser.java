package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.blog.entity.AuthProvider;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullInfoUser {

    private Long id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String email;
    private String avatar;
    private String address;
    private boolean mfaEnabled;

    @JsonProperty("auth_provider")
    private AuthProvider authProvider;


}
