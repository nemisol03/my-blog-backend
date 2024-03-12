package com.springboot.blog.service.impl;


import com.springboot.blog.entity.ConfirmationToken;
import com.springboot.blog.repository.ConfirmationTokenRepository;
import com.springboot.blog.service.ConfirmationTokenService;
import com.springboot.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepo;

    private final UserService userService;
    @Override
    public void saveToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepo.save(confirmationToken);
    }

    @Override
    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepo.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));
        if(confirmationToken.getConfirmedAt()!=null) {
            throw new IllegalStateException("Email already confirmed!");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiredAt();
        // if token is expired
        if(LocalDateTime.now().isAfter(expiredAt)) {
            throw new IllegalStateException("Token expired!");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepo.save(confirmationToken);
        userService.confirmEmail(confirmationToken.getUser().getEmail());

    }
}
