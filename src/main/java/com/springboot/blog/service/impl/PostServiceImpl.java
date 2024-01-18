package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    private final ModelMapper modelMapper;


    @Override
    public PostDTO createPost(PostDTO postDTO)  {
        Post post = modelMapper.map(postDTO,Post.class);

        Post savedPost = postRepository.save(post);

        PostDTO postResponse =modelMapper.map(savedPost,PostDTO.class);
        postResponse.setModifiedDate(null);
        postResponse.setModifiedBy(null);
        return postResponse;
    }

    @Override
    public PostResponse listAllPost(int pageNo, int pageSize,String sortDir,String sortField) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNo -1,pageSize,sort);

        Page<Post> page = postRepository.findAll(pageable);
        List<PostDTO> content = page.getContent().stream().map(post -> modelMapper.map(post,PostDTO.class)).toList();
        long totalElements = page.getTotalElements();
        int totalPage = page.getTotalPages();
        boolean isLast = page.isLast();
        PostResponse postResponse = PostResponse.builder().content(content)
                .totalElements(totalElements)
                .totalPage(totalPage)
                .last(isLast)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .build();

        return postResponse;
    }



    @Override
    public PostDTO findById(Long id) throws ResourceNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Couldn't find any post with the given id: " + id));
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(Long id, PostDTO postDT0) throws ResourceNotFoundException {
        Post existingPost = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Couldn't find any post with the given id: " + id));
     /*create a new createdDate var in order to keep the old value of the existing Post, because when saved Post var is updated.
      It will modify the existing Post(both of them point to the same ADDRESS)
     * */
        LocalDateTime createdDate = existingPost.getCreatedDate();
        Long createBy = existingPost.getCreatedBy();;
        postDT0.setId(id);
        Post post = modelMapper.map(postDT0,Post.class);
        Post savedPost = postRepository.save(post);
        PostDTO postResponse = modelMapper.map(savedPost, PostDTO.class);
        postResponse.setCreatedDate(createdDate);
        postResponse.setCreatedBy(createBy);
        return postResponse ;
    }

    @Override
    public void deletePost(Long id) throws ResourceNotFoundException {
        postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Couldn't find any post with the given id: " + id));
        postRepository.deleteById(id);

    }
}
