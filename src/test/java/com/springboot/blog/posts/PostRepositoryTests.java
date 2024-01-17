package com.springboot.blog.posts;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class PostRepositoryTests {
    @Autowired
    private PostRepository postRepo;

    @Test
    public void createNewPost() {
        Post post = new Post();
        post.setTitle("This is a title");
        post.setDescription("description here...");
        post.setContent("This is a content of the post");
        Post savedPost = postRepo.save(post);
        Assertions.assertThat(savedPost.getId()).isGreaterThan(0);
    }


    @Test
    public void testGetAllCommentOfSpecificPost() {
        long postId = 1;
        Optional<Post> postExisting = postRepo.findById(postId);
        Assertions.assertThat(postExisting.isPresent());
        Set<Comment> comments = postExisting.get().getComments();
        comments.forEach(System.out::println);
    }
}
