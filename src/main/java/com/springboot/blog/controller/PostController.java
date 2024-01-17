package com.springboot.blog.controller;

import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.exception.ResponseError;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) throws URISyntaxException {
        PostDTO post = postService.createPost(postDTO);
//        URI uri = new URI("/api/posts/" + post.getId());
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();  // more robust way
        return ResponseEntity.created(uri).body(post);
    }



    @GetMapping
    public ResponseEntity<?> listByPage(
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortDir",required = false,defaultValue = "asc") String sortDir,
            @RequestParam(value = "sortField",required = false,defaultValue = "id") String sortField ) {

            PostResponse postResponse = postService.listAllPost(pageNo, pageSize,sortDir,sortField);

        if (postResponse.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findPost(@PathVariable("id") Long id) {
        try {
            PostDTO postDTO = postService.findById(id);
            return new ResponseEntity<>(postDTO, HttpStatus.OK);

        } catch (ResourceNotFoundException ex) {
            return ControllerUtils.handleResourceNotFoundException(ex, id, "Post");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@RequestBody PostDTO postDTO, @PathVariable("id") Long id) {
        try {
            PostDTO updatedPost = postService.updatePost(id, postDTO);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);


        } catch (ResourceNotFoundException ex) {
            return ControllerUtils.handleResourceNotFoundException(ex, id, "Post");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok().build();

        } catch (ResourceNotFoundException ex) {
            return ControllerUtils.handleResourceNotFoundException(ex, id, "Post");
        }
    }

}
