package com.springboot.blog.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.blog.utils.UserEntityListener;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EntityListeners({AuditingEntityListener.class, UserEntityListener.class})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;


    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
    private List<Comment> children = new ArrayList<>();



    @CreatedDate
    @Column(nullable = false,
            updatable = false)
    @JsonProperty("created_date")
    private LocalDateTime createdDate;


    @LastModifiedDate
    @Column(insertable = false)
    @JsonProperty("modified_date")
    private LocalDateTime modifiedDate;


    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", user=" + user.getId() +
                ", parent=" + (parent != null ? parent.getId().toString() : "null") +
                ", children=" + children.toString() +
                '}';
    }

}
