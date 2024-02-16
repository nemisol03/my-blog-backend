package com.springboot.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.blog.utils.UserEntityListener;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "tags")
@EntityListeners(UserEntityListener.class)
public class Tag extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String name;

    private String description;



    @JsonIgnore
    @OneToMany(mappedBy = "tag",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Post> posts ;


}
