package com.springboot.blog.service;

import com.springboot.blog.entity.AuthenticationRequest;
import com.springboot.blog.entity.AuthenticationResponse;
import com.springboot.blog.entity.RegisterRequest;

public interface AuthService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse register(RegisterRequest request);
}
