package com.springboot.blog.service;

import com.springboot.blog.payload.auth.AuthenticationRequest;
import com.springboot.blog.payload.auth.AuthenticationResponse;
import com.springboot.blog.payload.auth.RegisterRequest;
import com.springboot.blog.payload.auth.VerificationRequest;
import com.springboot.blog.exception.UniqueFieldViolationException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;

import java.io.UnsupportedEncodingException;

public interface AuthService {
    AuthenticationResponse authenticate(AuthenticationRequest request) throws BadRequestException;
    void register(HttpServletRequest servletRequest,RegisterRequest request) throws UniqueFieldViolationException, MessagingException, UnsupportedEncodingException;

    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response);

    AuthenticationResponse verifyCode(VerificationRequest verificationRequest);
}
