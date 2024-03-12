package com.springboot.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;
    private String avatar;
    private String description;

    @Column(length = 64)  // Can be null because OAuth2 authentication can be implemented later
    private String password;

    private String address;
    private String secret;
    private boolean mfaEnabled;


    @JsonIgnore
    @Column(columnDefinition = "DEFAULT false")
    private boolean enabled;
    @JsonIgnore
    private boolean trashed;

    private boolean confirmedEmail;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    private String providerId;


    @Transient
    public User setName(String name,User user) {
        String[] words = name.split(" ");
        if(words.length >1) {
            String firstName = words[0];
            String lastName = name.substring(firstName.length());
            user.setFirstName(firstName);
            user.setLastName(lastName);
        }else {
            user.setFirstName(words[0]);
            user.setLastName("");
        }
        return user;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(

            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();


//    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
//    private List<Token> tokens;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (var role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName().name()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
