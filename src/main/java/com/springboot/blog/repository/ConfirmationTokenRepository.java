package com.springboot.blog.repository;

import com.springboot.blog.entity.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken,Long>  {
    Optional<ConfirmationToken> findByToken(String token);
}
