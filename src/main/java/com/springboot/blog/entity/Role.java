package com.springboot.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 128,nullable = false,unique = true)
    @Enumerated(EnumType.STRING)
    private UserRole name;
    @Length(min=10,max = 255,message = "The description must be greater or equal 10 characters")
    private String description;

    public Role(UserRole name) {
this.name = name;
    }


    @Override
    public String toString() {
        return name.name();
    }
}
