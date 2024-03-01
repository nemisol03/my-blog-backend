package com.springboot.blog.service;

public interface TwoFactorAuthenticationService {
    String generateQrImageUri(String secret);
    String generateNewSecret();
    boolean isOtpValid(String secret,String code);
    boolean isOtpNotValid(String secret,String code);
}
