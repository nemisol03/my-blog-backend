package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepo;
    private final PostRepository postRepo;
    private final ModelMapper modelMapper;

    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) throws ResourceNotFoundException {
        Post post = postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post not found with id: " + postId));
        Comment newCmt = modelMapper.map(commentDTO,Comment.class);
        newCmt.setPost(post);
        Comment savedCmt = commentRepo.save(newCmt);

        return modelMapper.map(savedCmt, CommentDTO.class);
    }

    @Override
    public CommentDTO getCommentById(Long commentId) {
        return null;
    }

    @Override
    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO) throws ResourceNotFoundException {
       Comment existingCmt = commentRepo.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment not found with id: " + commentId));

        existingCmt.setMessage(commentDTO.getMessage());
        Comment savedCmt = commentRepo.save(existingCmt);

        return modelMapper.map(savedCmt, CommentDTO.class);
    }

    @Override
    public void deleteComment(Long commentId) throws ResourceNotFoundException {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        commentRepo.delete(comment);
    }
}
