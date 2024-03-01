//package com.springboot.blog.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Getter
//@Setter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name= "tokens")
//public class Token {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(columnDefinition = "TEXT")
//    private String token;
//    @Enumerated(EnumType.STRING)
//    private TokenType tokenType;
//    private boolean expired;
//    private boolean revoked;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//}
