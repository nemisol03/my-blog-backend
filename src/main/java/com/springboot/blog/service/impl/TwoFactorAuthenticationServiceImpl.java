package com.springboot.blog.service.impl;


import com.springboot.blog.service.TwoFactorAuthenticationService;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwoFactorAuthenticationServiceImpl implements TwoFactorAuthenticationService {
    @Override
    public String generateQrImageUri(String secret) {
        QrData data = new QrData.Builder()
                .label("hello")
                .secret(secret)
                .issuer("Vu Hoai Nam")
                .algorithm(HashingAlgorithm.SHA1)
                .period(30)
                .digits(6)
                .build();
        QrGenerator generator = new ZxingPngQrGenerator();
        try {
            byte [] imageData = generator.generate(data);
            return Utils.getDataUriForImage(imageData,generator.getImageMimeType());
        } catch (QrGenerationException e) {
            log.error("failed to generate qr code image!");
            throw new RuntimeException(e);
        }

    }

    @Override
    public String generateNewSecret() {
        SecretGenerator generator = new DefaultSecretGenerator();
        return generator.generate();
    }

    @Override
    public boolean isOtpValid(String secret, String code) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret,code);
    }

    @Override
    public boolean isOtpNotValid(String secret, String code) {
        return !isOtpValid(secret,code);
    }
}
