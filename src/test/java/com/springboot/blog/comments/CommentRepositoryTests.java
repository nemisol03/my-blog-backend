package com.springboot.blog.comments;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.repository.CommentRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

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
        Comment comment = new Comment();
        comment.setContent("This post is very interesting!");
        Post post = entityManager.find(Post.class, postId);
        comment.setPost(post);
        Comment savedComment = commentRepo.save(comment);
        Assertions.assertThat(savedComment.getId()).isGreaterThan(0);
        Assertions.assertThat(savedComment.getPost().getId()).isEqualTo(postId);
    }

}
