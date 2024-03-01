package com.springboot.blog.payload.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "First name can't be blank")
    @Length(min = 2, max = 30, message = "First name must be between 3 and 30 characters")
    @JsonProperty("first_name")
    private String firstName;
    @NotBlank(message = "Last name can't be blank")
    @Length(min = 2, max = 30, message = "Last name must be between 3 and 30 characters")
    @JsonProperty("last_name")
    private String lastName;
    @NotBlank(message = "Email can't be blank")
    @Length(min = 10, max = 255, message = "Email must be between 10 and 255 characters")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password can't be blank")
    private String password;
    private String address;
}
