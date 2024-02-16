package com.springboot.blog.controller;

import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.service.impl.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentServiceImpl commentService;
    @PostMapping
    public ResponseEntity<?> createCmt(@RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO comment = commentService.createComment(commentDTO.getPostId(), commentDTO);
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequestUri()
                    .path("/{id}")
                    .buildAndExpand(comment.getId()).toUri();
            return ResponseEntity.created(uri).body(comment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCmt(@PathVariable("id") Long cmtId,@RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO comment = commentService.updateComment(cmtId, commentDTO);
            return ResponseEntity.ok(comment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCmt(@PathVariable("id") Long cmtId) {
        try {
            commentService.deleteComment(cmtId);
            return ResponseEntity.noContent().build() ;
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}





