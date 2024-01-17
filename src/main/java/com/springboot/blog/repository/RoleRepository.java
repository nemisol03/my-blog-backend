package com.springboot.blog.repository;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(UserRole name);
}
