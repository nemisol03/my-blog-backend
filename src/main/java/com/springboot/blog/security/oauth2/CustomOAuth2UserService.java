package com.springboot.blog.security.oauth2;


import com.springboot.blog.entity.AuthProvider;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.Oauth2AuthenticationProcessionException;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.UserPrincipal;
import com.springboot.blog.security.oauth2.user.Oauth2UserInfo;
import com.springboot.blog.security.oauth2.user.Oauth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // trigger OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }


    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        Oauth2UserInfo oauth2UserInfo = Oauth2UserInfoFactory
                .getOauth2UserInfo(userRequest.getClientRegistration().getRegistrationId(),
                        oAuth2User.getAttributes());
        if (StringUtils.isEmpty(oauth2UserInfo.getEmail())) {
            throw new Oauth2AuthenticationProcessionException("Could not find email from oauth2 provider");
        }
        Optional<User> userOptional = userRepo.findByEmail(oauth2UserInfo.getEmail());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getAuthProvider().equals(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()))) {
                throw new Oauth2AuthenticationProcessionException("Looks like you're signed up with " +
                        user.getAuthProvider() + " account. Please use your " + user.getAuthProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oauth2UserInfo);
        } else {
            user = registerNewUser(userRequest, oauth2UserInfo);
        }

        return UserPrincipal.create(user,oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest userRequest, Oauth2UserInfo oauth2UserInfo) {
        User user = new User();
        user.setName(oauth2UserInfo.getName(), user);
        user.setEmail(oauth2UserInfo.getEmail());
        user.setAuthProvider(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oauth2UserInfo.getId());
        user.setAvatar(oauth2UserInfo.getImageUrl());

        return userRepo.save(user);
    }

    private User updateExistingUser(User existingUser, Oauth2UserInfo oauth2UserInfo) {
        existingUser.setAvatar(oauth2UserInfo.getImageUrl());
        existingUser.setName(oauth2UserInfo.getName(), existingUser);

        return userRepo.save(existingUser);
    }


}
