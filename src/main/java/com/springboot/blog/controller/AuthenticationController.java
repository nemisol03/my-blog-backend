package com.springboot.blog.controller;

import com.springboot.blog.exception.ErrorDTO;
import com.springboot.blog.exception.UniqueFieldViolationException;
import com.springboot.blog.payload.auth.AuthenticationRequest;
import com.springboot.blog.payload.auth.AuthenticationResponse;
import com.springboot.blog.payload.auth.RegisterRequest;
import com.springboot.blog.payload.auth.VerificationRequest;
import com.springboot.blog.service.AuthService;
import com.springboot.blog.service.ConfirmationTokenService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;
    private final ConfirmationTokenService confirmationTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(HttpServletRequest servletRequest, @RequestBody @Valid RegisterRequest req) {
        try {
            authService.register(servletRequest, req);
            return ResponseEntity.ok("Registration successful!");
        } catch (UniqueFieldViolationException e) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setTimestamp(new Date());
            errorDTO.setPath("/register");
            errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            errorDTO.addError(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        } catch (MessagingException | UnsupportedEncodingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to complete registration. Please try again later.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticationRequest req) {
        try {
            return ResponseEntity.ok(authService.authenticate(req));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
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

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(HttpServletRequest request) {
        String token = request.getParameter("token");

        try {
            confirmationTokenService.confirmToken(token);
            return ResponseEntity.ok().body("Email verified successfully!");
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

    }


}
