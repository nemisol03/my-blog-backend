package com.springboot.blog.service;

import com.springboot.blog.entity.AuthenticationRequest;
import com.springboot.blog.entity.AuthenticationResponse;
import com.springboot.blog.entity.RegisterRequest;
import com.springboot.blog.exception.UniqueFieldViolationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    void register(RegisterRequest request) throws UniqueFieldViolationException;

    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
}
