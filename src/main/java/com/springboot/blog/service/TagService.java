package com.springboot.blog.service;

import com.springboot.blog.entity.Tag;
import com.springboot.blog.exception.ResourceInUseException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.TagDTO;

import java.util.List;

public interface TagService {
    List<Tag> listAllTag();


    TagDTO findById(Long id) throws ResourceNotFoundException;

    TagDTO saveTag(TagDTO tagDTO) throws ResourceInUseException;

    void deleteTag(Long id) throws ResourceNotFoundException;

    Long countTag();
    TagDTO updateTag(Long id, TagDTO tagDTO) throws ResourceNotFoundException;

}
