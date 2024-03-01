package com.springboot.blog.service;

import com.springboot.blog.payload.auth.AuthenticationRequest;
import com.springboot.blog.payload.auth.AuthenticationResponse;
import com.springboot.blog.payload.auth.RegisterRequest;
import com.springboot.blog.payload.auth.VerificationRequest;
import com.springboot.blog.exception.UniqueFieldViolationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    void register(RegisterRequest request) throws UniqueFieldViolationException;

    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response);

    AuthenticationResponse verifyCode(VerificationRequest verificationRequest);
}
