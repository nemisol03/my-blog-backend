package com.springboot.blog.controller;

import com.springboot.blog.payload.auth.AuthenticationRequest;
import com.springboot.blog.payload.auth.AuthenticationResponse;
import com.springboot.blog.payload.auth.RegisterRequest;
import com.springboot.blog.exception.ErrorDTO;
import com.springboot.blog.exception.UniqueFieldViolationException;
import com.springboot.blog.payload.ResponseMessage;
import com.springboot.blog.payload.auth.VerificationRequest;
import com.springboot.blog.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {
        try {
            authService.register(req);
            return ResponseEntity.ok(new ResponseMessage("Register successfully!"));
        } catch (UniqueFieldViolationException e) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setTimestamp(new Date());
            errorDTO.setPath("/register");
            errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            errorDTO.addError(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest req) {
        return ResponseEntity.ok(authService.authenticate(req));

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request,HttpServletResponse response) {
        AuthenticationResponse authenticationResponse = authService.refreshToken(request, response);

        if (authenticationResponse != null) {
            return ResponseEntity.ok(authenticationResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest verificationRequest) {
        return ResponseEntity.ok(authService.verifyCode(verificationRequest));

    }
}
