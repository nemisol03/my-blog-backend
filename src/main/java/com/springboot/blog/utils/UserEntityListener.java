package com.springboot.blog.utils;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.User;
import jakarta.persistence.PrePersist;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserEntityListener {
    @PrePersist
    public void prePersist(Object object) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
                User loggedInUser = (User) authentication.getPrincipal();
            if (object instanceof Post) {
                Post post = (Post) object;
                post.setUser(loggedInUser);
            }else if(object instanceof Comment) {
                Comment cmt = (Comment) object;
                cmt.setUser(loggedInUser);
            }
        }
    }

}
