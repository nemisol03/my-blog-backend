package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.Tag;
import com.springboot.blog.exception.ResourceInUseException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.TagDTO;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.repository.TagRepository;
import com.springboot.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class TagServiceImpl implements TagService {
    private final TagRepository tagRepo;

    private final PostRepository postRepo;
    private final ModelMapper modelMapper;


    @Override
    public List<Tag> listAllTag() {
        return tagRepo.findAll();
    }

    @Override
    public TagDTO findById(Long id) throws ResourceNotFoundException {
        Tag tag = tagRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find any tag with the given id: " + id));
        return modelMapper.map(tag,TagDTO.class);
    }

    @Override
    public TagDTO saveTag(TagDTO tagDTO) throws ResourceInUseException {
        if(!isUniqueTag(tagDTO.getId(),tagDTO.getName()) ) {
            throw new ResourceInUseException("The specified tag is already taken.");
        }
        Tag tag = modelMapper.map(tagDTO,Tag.class);
        Tag savedTag = tagRepo.save(tag);

        return modelMapper.map(savedTag, TagDTO.class);
    }

    @Override
    public void deleteTag(Long id) throws ResourceNotFoundException {
        tagRepo.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Could not find any tag with the given id: " + id));
        List<Post> availablePosts = postRepo.findAllByTagId(id);
        if(!availablePosts.isEmpty()) {
            throw new DataIntegrityViolationException("Cannot delete tag with ID " + id + ". It is referenced by one or more posts.");
        }

        tagRepo.deleteById(id);


    }

    @Override
    public Long countTag() {
        return tagRepo.count();
    }

    @Override
    public TagDTO updateTag(Long id, TagDTO tagDTO) throws ResourceNotFoundException {
        Tag tag = tagRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find any tag with the given id: " + id));
        tag.setName(tagDTO.getName());
        tag.setDescription(tagDTO.getDescription());
        Tag savedTag = tagRepo.save(tag);

        return modelMapper.map(savedTag,TagDTO.class);
    }

    private boolean isUniqueTag(Long id,String name) {
        Tag findByName = tagRepo.findByName(name);
        if(findByName == null) {
            return true;
        }
        boolean isCreatingNew = id == null;
        if(isCreatingNew) {
            return false;
        }else {
            if(findByName.getId()!=id) {
                return false;
            }
        }
        return true;
    }
}
