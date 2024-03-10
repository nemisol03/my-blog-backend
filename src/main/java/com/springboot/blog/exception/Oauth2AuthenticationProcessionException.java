package com.springboot.blog.exception;


import org.springframework.security.core.AuthenticationException;

public class Oauth2AuthenticationProcessionException extends AuthenticationException {
public Oauth2AuthenticationProcessionException(String msg) {
    super(msg);
}

public Oauth2AuthenticationProcessionException(String msg,Throwable t) {super(msg,t);}




}
