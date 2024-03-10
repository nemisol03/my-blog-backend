package com.springboot.blog.security.oauth2.user;


import com.springboot.blog.entity.AuthProvider;
import com.springboot.blog.exception.Oauth2AuthenticationProcessionException;

import java.util.Map;

public class Oauth2UserInfoFactory {

    public static Oauth2UserInfo getOauth2UserInfo(String registrationId, Map<String,Object> attributes)  {
        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOauth2UserInfo(attributes);
        }else if(registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
            return new FacebookOauth2UserInfo(attributes);
        }else if(registrationId.equalsIgnoreCase(AuthProvider.github.toString()))    {
            return new GithubOauth2UserInfo(attributes);
        }else {
            throw new Oauth2AuthenticationProcessionException("Sorry! Login with " + registrationId + " is not supported yet!");
        }

    }
}
