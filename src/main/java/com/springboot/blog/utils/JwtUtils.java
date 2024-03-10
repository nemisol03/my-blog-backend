package com.springboot.blog.utils;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.payload.FullInfoUser;
import com.springboot.blog.service.impl.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JwtUtils {
    public static String constructToken(UserDetails principal, User user, ModelMapper modelMapper, JwtService jwtService) {
        FullInfoUser fullInfoUser = modelMapper.map(user, FullInfoUser.class);
        Map<String, Object> claims = new HashMap<>();
        Set<Role> roles = user.getRoles();
        List<String> rolesName = roles.stream().map(role -> role.getName().name()).toList();
        claims.put("user", fullInfoUser);
        claims.put("roles", rolesName);

        return jwtService.generateToken(claims,principal);
    }
}
