package com.springboot.blog.controller;

import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.payload.FullInfoPost;
import com.springboot.blog.payload.PageResponse;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.UploadResource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final UploadResource uploadResource;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(@ModelAttribute PostDTO postDTO,
                                        @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws URISyntaxException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String uploadDir = "posts/" + postDTO.getSlug();
            String url = uploadResource.uploadImage(multipartFile, uploadDir);
            postDTO.setThumbnail(url);
        }
        try {
            PostDTO post = postService.createPost(postDTO);
            URI uri = new URI("/api/posts/" + post.getId());

            return ResponseEntity.created(uri).body(post);
        } //check slug must be unique
        catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } // tag must exist
        catch (ResourceNotFoundException | InvalidDataAccessApiUsageException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping({"", "/", "/manage/all"})
    public ResponseEntity<?> listByPage(
            HttpServletRequest request,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(value = "q", required = false) String keyword) {


        System.out.println(request.getContextPath());
        PageResponse<FullInfoPost> postResponse;
        if (request.getRequestURI().equals("/api/v1/posts/manage/all")) {
            postResponse = postService.listByPage(pageNo, pageSize, sortDir, sortField,keyword);
        } else {
            postResponse = postService.listAllApprovedPost(pageNo, pageSize, sortDir, sortField);

        }

        if (postResponse.getData().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(postResponse);
    }


    @GetMapping("/{slug}")
    public ResponseEntity<?> findPost(@PathVariable("slug") String slug) {
        try {
            FullInfoPost post;
            //based on ID
                post = postService.findBySlug(slug);
            return new ResponseEntity<>(post, HttpStatus.OK);

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> findPostById(@PathVariable("id") Long id) {
        try {
            FullInfoPost post;
            //based on ID
            post = postService.findById(id);
            return new ResponseEntity<>(post, HttpStatus.OK);

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countPost() {
        return ResponseEntity.ok(postService.countPost());
    }

    @GetMapping("/latest")
    public ResponseEntity<FullInfoPost> getLatestPost() {
        return ResponseEntity.ok(postService.getLatest());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<FullInfoPost>> getRecentPosts() {
        List<FullInfoPost> list = postService.listRecentPosts();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/trashed")
    public ResponseEntity<List<PostDTO>> getTrashedPosts() {
        List<PostDTO> postList = postService.listTrashedPosts();
        return ResponseEntity.ok(postList);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable("id") Long postId) {
        List<CommentDTO> list = postService.listCommentsByPosts(postId);
        if(list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}/related/{tagId}")
    public ResponseEntity<List<FullInfoPost>> listRelatedPosts(@PathVariable Long id,@PathVariable Long tagId) {
        List<FullInfoPost> list = postService.listRelatedPosts(id,tagId);
        if(list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@ModelAttribute PostDTO postDTO, @PathVariable("id") Long id,
                                        @RequestParam(value = "image", required = false) MultipartFile multipartFile) {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String uploadDir = "posts/" + postDTO.getSlug();
            String url = uploadResource.uploadImage(multipartFile, uploadDir);
            postDTO.setThumbnail(url);
        }
        try {
            PostDTO updatedPost = postService.updatePost(id, postDTO);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);


        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> trashPost(@PathVariable("id") Long id) {
        try {
            postService.trashPost(id);
            return ResponseEntity.ok().build();

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/delete-permanently")
    public ResponseEntity<?> deletePermanentlyPost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok().build();

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }


    @PatchMapping("/{id}/trashed/{trashed}")
    public ResponseEntity<?> updateTrashed(@PathVariable("id") Long id, @PathVariable("trashed") boolean trashed) {
        try {
            postService.updateTrashed(id, trashed);
            return ResponseEntity.ok().build();

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }
    @GetMapping("/hot")
    public ResponseEntity<?> getHotPosts() {
        List<FullInfoPost> list = postService.listHotPosts();
        if(list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}
