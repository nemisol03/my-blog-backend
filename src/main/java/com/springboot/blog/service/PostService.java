package com.springboot.blog.service;

import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.payload.FullInfoPost;
import com.springboot.blog.payload.PageResponse;
import com.springboot.blog.payload.PostDTO;

import java.util.List;


public interface PostService {
    PostDTO createPost(PostDTO postDTO) throws ResourceNotFoundException;

    PageResponse<FullInfoPost> listByPage(int pageNo, int pageSize, String sortDir, String sortField, String keyword);
    PageResponse<FullInfoPost> listAllApprovedPost(int pageNo, int pageSize, String sortBy, String sortField);

    FullInfoPost findById(Long id) throws ResourceNotFoundException;
    FullInfoPost findBySlug(String slug) throws ResourceNotFoundException;

    PostDTO updatePost(Long id,PostDTO postDT0) throws ResourceNotFoundException;
    void deletePost(Long id) throws ResourceNotFoundException;

    Long countPost();
    FullInfoPost getLatest();

    List<FullInfoPost> listRecentPosts();
    List<PostDTO> listTrashedPosts();

    void updateTrashed(Long id, boolean trashed) throws ResourceNotFoundException;

    void trashPost(Long id) throws ResourceNotFoundException;

    List<FullInfoPost> listRelatedPosts(Long id, Long tagId);

    List<CommentDTO> listCommentsByPosts(Long postId);

    List<FullInfoPost> listHotPosts();
}
