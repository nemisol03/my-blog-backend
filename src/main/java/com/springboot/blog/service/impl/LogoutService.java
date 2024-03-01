//package com.springboot.blog.service.impl;
//
//import com.springboot.blog.entity.Token;
//import com.springboot.blog.repository.TokenRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class LogoutService implements LogoutHandler {
//    private final TokenRepository tokenRepo;
//    @Override
//    public void logout(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Authentication authentication) {
//        final String authHeader = request.getHeader("Authorization");
//        final String jwt;
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        jwt = authHeader.substring(7);
//        Token storedToken = tokenRepo.findByToken(jwt).orElse(null);
//        if(storedToken != null) {
//            storedToken.setExpired(true);
//            storedToken.setExpired(true);
//            tokenRepo.save(storedToken);
//        }
//    }
//}
