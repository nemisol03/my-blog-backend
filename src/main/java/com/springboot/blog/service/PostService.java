package com.springboot.blog.service;

import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import org.springframework.stereotype.Service;


public interface PostService {
    PostDTO createPost(PostDTO postDTO);

    PostResponse listAllPost(int pageNo,int pageSize,String sortBy,String sortField);

    PostDTO findById(Long id) throws ResourceNotFoundException;

    PostDTO updatePost(Long id,PostDTO postDT0) throws ResourceNotFoundException;
    void deletePost(Long id) throws ResourceNotFoundException;
}
