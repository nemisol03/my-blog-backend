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

        parent.setPost(post);
        Comment savedComment = commentRepo.save(parent);

    }

    @Test
    public void testListCommentByPostId() {
        long postId = 9;
        List<Comment> comments = commentRepo.findAllByPostIdOrderByCreatedAtDesc(postId);
        comments.forEach(System.out::println);
    }

}
