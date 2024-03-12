package com.springboot.blog.service;

import com.springboot.blog.entity.ConfirmationToken;
import com.springboot.blog.entity.User;

public interface ConfirmationTokenService {
    void saveToken(ConfirmationToken confirmationToken);

    void confirmToken(String token);
}
