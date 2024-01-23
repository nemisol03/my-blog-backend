package com.springboot.blog.comments;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.CommentRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CommentRepositoryTests {
    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private EntityManager entityManager;
    @Test
    public void testCreateComment() {
        long postId = 1;
        long userId = 2;
        Comment comment = new Comment();
        comment.setMessage("The second comment");
        Post post = entityManager.find(Post.class, postId);
        User user = entityManager.find(User.class,userId);
        comment.setPost(post);
        comment.setUser(user);
        Comment savedComment = commentRepo.save(comment);
        Assertions.assertThat(savedComment.getId()).isGreaterThan(0);
        Assertions.assertThat(savedComment.getPost().getId()).isEqualTo(postId);
    }

    @Test
    public void testNestedComments() {
        long cmtId = 1;
        User user1 = entityManager.find(User.class,1);
        User user2 = entityManager.find(User.class,2);
        Post post = entityManager.find(Post.class,1);
        Comment parent = entityManager.find(Comment.class,cmtId);

        Comment child1 = Comment.builder()
                .message("this is a comment which belongs to parent id:  "  + parent.getId())
                .user(user1).parent(parent).post(post)
                .build();

        Comment child2 = Comment.builder()
                .message("this is a comment which belongs to parent id:  "  + parent.getId())
                .user(user1).parent(parent).post(post)
                .build();
        Comment childOfChild2 = Comment.builder().message("this is a comment which belongs to parent id:  "  + child2.getId())
                .user(user2)
                .parent(child2).post(post)
                .build();
        List<Comment> childrenCmts = new ArrayList<>();
        childrenCmts.add(child1);
        childrenCmts.add(child2);
        List<Comment> childrenCmts2 =new ArrayList<>();
        childrenCmts2.add(childOfChild2);
        parent.setChildren(childrenCmts);
        child2.setChildren(childrenCmts2);
        parent.setPost(post);
        Comment savedComment = commentRepo.save(parent);
        Assertions.assertThat(savedComment.getChildren().size()).isGreaterThanOrEqualTo(2);
        Assertions.assertThat(child2.getChildren().size()).isEqualTo(1);

    }

}
