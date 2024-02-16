package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private Long id;
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String password;

    private String avatar;
    private String address;
    @Size(max = 255,message = "The description must be less than 255 characters")
    private String description;

    private long numOfPosts;
    private long numOfComments;

    private boolean enabled;
}
