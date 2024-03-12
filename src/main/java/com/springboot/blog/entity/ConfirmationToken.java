package com.springboot.blog.entity;

import com.springboot.blog.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "confirmation_tokens")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
//    @Min(value = 32,message = "The length of token must be at least 32 characters")
    @Column(nullable = false)
    private String token;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    @NotNull
    private LocalDateTime expiredAt;


    @ManyToOne
    private User user;




}
