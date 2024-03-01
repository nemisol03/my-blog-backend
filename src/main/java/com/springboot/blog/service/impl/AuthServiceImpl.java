package com.springboot.blog.service.impl;

import com.springboot.blog.entity.*;
import com.springboot.blog.exception.UniqueFieldViolationException;
import com.springboot.blog.payload.FullInfoUser;
import com.springboot.blog.payload.auth.AuthenticationRequest;
import com.springboot.blog.payload.auth.AuthenticationResponse;
import com.springboot.blog.payload.auth.RegisterRequest;
import com.springboot.blog.payload.auth.VerificationRequest;
import com.springboot.blog.repository.RoleRepository;
//import com.springboot.blog.repository.TokenRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.AuthService;
import com.springboot.blog.service.TwoFactorAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final RoleRepository roleRepo;
    //    private final TokenRepository tokenRepo;
    private final ModelMapper modelMapper;
    private final TwoFactorAuthenticationService tfaService;


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
        ));

        User user = (User) authentication.getPrincipal();
        if (user.isMfaEnabled()) {
            return AuthenticationResponse.builder()
                    .token("")
                    .refreshToken("")
                    .secretImageUri(tfaService.generateQrImageUri(user.getSecret()))
                    .mfaEnabled(true)
                    .build();
        }
        return getAuthenticationResponse(user);

    }

//    private void revokedAllValidUserToken(User user) {
//        List<Token> validUserTokens = tokenRepo.findAllValidTokensByUser(user.getId());
//        if (!validUserTokens.isEmpty()) {
//            validUserTokens.forEach(token -> {
//                token.setExpired(true);
//                token.setRevoked(true);
//            });
//        }
//
//
//        tokenRepo.saveAll(validUserTokens);
//    }

    @Override
    public void register(RegisterRequest request) throws UniqueFieldViolationException {
        // before that,let's checking email must be unique
        String email = request.getEmail();
        Optional<User> existingUser = userRepo.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new UniqueFieldViolationException("This email: " + email + " address already exists, try with a different one");
        }

        UserRole roleDefault;
        //the first one
        if (userRepo.count() == 0) {
            roleDefault = UserRole.ADMIN;
        } else {
            // assign role: user by default
            roleDefault = UserRole.USER;
        }
        Role role = roleRepo.findByName(roleDefault)
                .orElseGet(() -> roleRepo.save(new Role(roleDefault)));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .roles(Collections.singleton(role))
                .enabled(true)
                .build();
        userRepo.save(user);

    }


    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = userRepo.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                FullInfoUser fullInfoUser = modelMapper.map(user, FullInfoUser.class);
                Map<String, Object> claims = new HashMap<>();
                Set<Role> roles = user.getRoles();
                List<String> rolesName = roles.stream().map(role -> role.getName().name()).toList();
                claims.put("user", fullInfoUser);
                claims.put("roles", rolesName);
                String token = jwtService.generateToken(claims, user);
//                revokedAllValidUserToken(user);

//                savedUserToken(user,token);
                return AuthenticationResponse.builder()
                        .token(token)
                        .refreshToken(refreshToken).build();
            }
        }
        return null;
    }

    @Override
    public AuthenticationResponse verifyCode(VerificationRequest verificationRequest) {
        User user = userRepo.findByEmail(verificationRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Could not find any user with the give email: %s !", verificationRequest.getEmail())));

        if (tfaService.isOtpNotValid(user.getSecret(), verificationRequest.getCode())) {
            throw new BadCredentialsException("Code is not correct!");
        }
        return getAuthenticationResponse(user);


    }

    private AuthenticationResponse getAuthenticationResponse(User user) {
        FullInfoUser fullInfoUser = modelMapper.map(user, FullInfoUser.class);
        Set<Role> roles = user.getRoles();
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesName = roles.stream().map(role -> role.getName().name()).toList();
        claims.put("user", fullInfoUser);
        claims.put("roles", rolesName);
        String token = jwtService.generateToken(claims, user);
        String refreshToken = jwtService.generateRefreshToken(claims, user);

        return AuthenticationResponse
                .builder()
                .token(token)
                .refreshToken(refreshToken)
                .mfaEnabled(user.isMfaEnabled())
                .build();
    }

//    private void savedUserToken(User user, String token) {
//        Token storeToken = Token.builder()
//                .token(token)
//                .tokenType(TokenType.BEARER)
//                .user(user)
//                .expired(false)
//                .revoked(false)
//                .build();
//        tokenRepo.save(storeToken);
//    }


}
