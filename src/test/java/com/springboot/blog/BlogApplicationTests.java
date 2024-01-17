package com.springboot.blog;

import com.springboot.blog.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BlogApplicationTests {

    @Autowired
    private Validator validator;

    @Test
    void testConstraints() {
        // Given
        User user = new User();
        user.setFirstName("    ");

        // When
        Errors errors = new BeanPropertyBindingResult(user, "user");
        ValidationUtils.invokeValidator(validator, user, errors);

        // Then
        assertThat(errors.hasErrors()).isFalse();
        assertThat(errors.getFieldError("firstName")).isNotNull();
    }
}
