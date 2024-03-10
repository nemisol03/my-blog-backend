package com.springboot.blog.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@ConfigurationProperties(prefix = "app")
@Component
@Getter
@ConfigurationProperties(prefix = "app")

public class AppProperties {

    private final Auth auth = new Auth();
    private final OAuth2 oAuth2 = new OAuth2();

    @Getter
    @Setter
    public static class Auth {

        @Value("${spring.jwt.secret-key}")
        private String tokenSecret;

        @Value("${spring.jwt.expiration}")
        private String tokenExpireMsec;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();
    }
}
