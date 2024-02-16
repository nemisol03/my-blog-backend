package com.springboot.blog.posts;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class PostRepositoryTests {
    @Autowired
    private PostRepository postRepo;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void createNewPost() {
        Post post = new Post();
        post.setTitle("This is a title that is created by user with user's id: 1");
//        post.setDescription("description here...");
        post.setContent("This is a content of the post");
        User user = entityManager.find(User.class,1);
        post.setUser(user);
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

    @Test
    public void testGetPost() {
        long postId = 1;
        Optional<Post> postExisting = postRepo.findById(postId);
        Assertions.assertThat(postExisting.isPresent());
        Post post = postExisting.get();
        System.out.println(post);
    }

    @Test
    public void testGetAllPosts() {
        List<Post> postList = postRepo.findAll();
        Assertions.assertThat(postList.size()).isGreaterThan(0);
    }

    @Test
    public void testUpdatePost() {
        long postId = 1;
        Optional<Post> postExisting = postRepo.findById(postId);
        Assertions.assertThat(postExisting.isPresent());
        Post post = postExisting.get();
        User user = entityManager.find(User.class,2);
        post.setUser(user);
        var title = "This post has been modified";
        post.setTitle(title);
        post.setContent("This post has been violated with our privacy,So We must be locked ");
        Post savedPost = postRepo.save(post);
        Assertions.assertThat(savedPost.getTitle()).isEqualTo(title);
    }

    @Test
    public void testDeletePost() {
        long postId = 2;
        boolean isExist = postRepo.existsById(postId);
        if(isExist) {
            postRepo.deleteById(postId);
            Optional<Post> dirtyPost = postRepo.findById(postId);
            Assertions.assertThat(dirtyPost).isNotPresent();
        }else {
            Assertions.fail("Post with ID " + postId + " does not exist.");
        }
    }
}
