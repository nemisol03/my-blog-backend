package com.springboot.blog.service;

import com.springboot.blog.entity.AuthenticationRequest;
import com.springboot.blog.entity.AuthenticationResponse;
import com.springboot.blog.entity.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
}
