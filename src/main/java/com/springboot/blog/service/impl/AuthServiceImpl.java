package com.springboot.blog.service.impl;

import com.springboot.blog.entity.*;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()
        ));


        User user = (User) authentication.getPrincipal();

        Set<Role> roles = user.getRoles();
        Map<String,Object> claims = new HashMap<>();
        List<String> rolesName = roles.stream().map(role-> role.getName().name()).toList();
        claims.put("roles",rolesName);
        LOGGER.info("Roles: " + rolesName);
        String token = jwtService.generateToken(claims,user);
        return AuthenticationResponse
                .builder()
                .token(token)
                .build();

    }

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        UserRole roleDefault = UserRole.ADMIN; // assign role: user by default
        Role role = roleRepo.findByName(roleDefault)
                .orElseGet(() -> roleRepo.save(new Role(roleDefault)));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(role))
                .build();
        User savedUser = userRepo.save(user);
        String token = jwtService.generateToken(savedUser);
        return AuthenticationResponse
                .builder()
                .token(token)
                .build();
    }
}
