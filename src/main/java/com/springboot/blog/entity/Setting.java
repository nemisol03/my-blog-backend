package com.springboot.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "settings")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    @NotNull
    @Column(nullable = false,name = "`key`")
    @EqualsAndHashCode.Include
    private String key;
    @NotNull
    @Column(columnDefinition = "TEXT",nullable = false)
    private String value;

    @Enumerated(EnumType.STRING)
    private SettingCategory category;

    public Setting(String key) {
        this.key = key;
    }
}
