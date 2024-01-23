package com.springboot.blog.service;

import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDTO;

public interface CommentService {

    CommentDTO createComment(Long postId, CommentDTO commentDTO) throws ResourceNotFoundException;

    CommentDTO getCommentById(Long commentId);

    CommentDTO updateComment(Long commentId, CommentDTO commentDTO) throws ResourceNotFoundException;

    void deleteComment(Long commentId) throws ResourceNotFoundException;
}
