package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.PostStatus;
import com.springboot.blog.entity.Tag;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.*;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.repository.TagRepository;
import com.springboot.blog.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepo;

    private final ModelMapper modelMapper;

    private final CommentRepository commentRepo;

    private final TagRepository tagRepo;


    @Override
    public PostDTO createPost(PostDTO postDTO) throws ResourceNotFoundException {
        checkUniqueSlug(postDTO.getSlug());

        Tag tag = tagRepo.findById(postDTO.getTagId()).orElseThrow(() -> new ResourceNotFoundException("Could not find any the given tag' id: " + postDTO.getTagId()));
        Post post = modelMapper.map(postDTO, Post.class);
        post.setTag(tag);
        post.setTrashed(false);

        Post savedPost = postRepo.save(post);

        PostDTO postResponse = modelMapper.map(savedPost, PostDTO.class);
//        postResponse.setModifiedAt(null);
        postResponse.setUserId(savedPost.getUser().getId());
        postResponse.setTagId(savedPost.getTag().getId());
        return postResponse;

    }

    @Override
    public PageResponse<FullInfoPost> listByPage(int pageNo, int pageSize, String sortDir, String sortField, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Post> page;
        if (keyword != null) {
            page = postRepo.search(keyword, pageable);
        } else {
            page = postRepo.findAllNotTrashedPosts(pageable);
        }
        return getPostResponse(pageNo, pageSize, page);
    }

    private PageResponse<FullInfoPost>  getPostResponse(int pageNo, int pageSize, Page<Post> page) {
        List<FullInfoPost> content = page.getContent().stream()
                .map(post -> modelMapper.map(post, FullInfoPost.class)).toList();
        long totalElements = page.getTotalElements();
        int totalPage = page.getTotalPages();
        boolean isLast = page.isLast();

// keep in mind that: specify a PageResponse type:)))
        PageResponse postResponse = PageResponse.<FullInfoPost>builder().data(content)
                .totalElements(totalElements)
                .totalPage(totalPage)
                .last(isLast)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .build();

        return postResponse;
    }

    @Override
    public PageResponse<FullInfoPost> listAllApprovedPost(int pageNo, int pageSize, String sortDir, String sortField) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<Post> page = postRepo.findAllApprovedPosts(pageable);
        return getPostResponse(pageNo, pageSize, page);
    }


    @Override
    public FullInfoPost findById(Long id) throws ResourceNotFoundException {
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Couldn't find any post with the given id: " + id));
        FullInfoPost postRes = modelMapper.map(post, FullInfoPost.class);

        return postRes;
    }

    @Override
    public FullInfoPost findBySlug(String slug) throws ResourceNotFoundException {
        Post post = postRepo.findBySlug(slug).orElseThrow(() -> new ResourceNotFoundException("Couldn't find any post with the given slug: " + slug));
        FullInfoPost postRes = modelMapper.map(post, FullInfoPost.class);
        return postRes;
    }

    @Override
    public PostDTO updatePost(Long id, PostDTO postDTO) throws ResourceNotFoundException {
        if(!checkUniqueSlug(postDTO)) {
            throw new DataIntegrityViolationException("Slug or title is in use. Please choose another one");
        }


        Post existingPost = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Couldn't find any post with the given id: " + id));
     /*create a new createdDate var in order to keep the old value of the existing Post, because when saved Post var is updated.
      It will modify the existing Post(both of them point to the same ADDRESS)
     * */
        existingPost.setSlug(postDTO.getSlug());
        existingPost.setTitle(postDTO.getTitle());
        existingPost.setStatus(PostStatus.valueOf(postDTO.getStatus()));
        existingPost.setHot(postDTO.isHot());
        existingPost.setTag(tagRepo.findById(postDTO.getTagId()).get());
        if(postDTO.getThumbnail()!= null) {
            existingPost.setThumbnail(postDTO.getThumbnail());
        }
        existingPost.setContent(postDTO.getContent());

        Post savedPost = postRepo.save(existingPost);
        PostDTO postResponse = modelMapper.map(savedPost, PostDTO.class);
        return postResponse;
    }

    @Override
    public void deletePost(Long id) throws ResourceNotFoundException {
        postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Couldn't find any post with the given id: " + id));
        postRepo.deleteById(id);
    }

    private boolean checkUniqueSlug(PostDTO postDTO) {
        Optional<Post> postBySlug = postRepo.findBySlug(postDTO.getSlug());
        Optional<Post> postById = postRepo.findById(postDTO.getId());
        if(!postBySlug.isPresent()) {
            return true;
        }

        if(postById.isPresent() && !postById.get().getSlug().equals(postBySlug.get().getSlug()) ) {
          return false;
        }

        return true;
    }

    @Override
    @Transactional
    public void trashPost(Long id) throws ResourceNotFoundException {
        postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Couldn't find any post with the given id: " + id));
        postRepo.updateTrashed(id,true);

    }

    @Override
    public List<FullInfoPost> listRelatedPosts(Long id, Long tagId) {
        List<Post> posts = postRepo.listRelatedPosts(id,tagId);
        List<FullInfoPost> list = posts.stream().map(post -> modelMapper.map(post, FullInfoPost.class)).toList();
        return list;
    }

    @Override
    public List<CommentDTO> listCommentsByPosts(Long postId) {
        List<Comment> comments = commentRepo.findAllByPostIdOrderByCreatedAtDesc(postId);
        List<CommentDTO>  list = comments.stream().map(cmt -> modelMapper.map(cmt, CommentDTO.class)).toList();
        return list;
    }

    @Override
    public List<FullInfoPost> listHotPosts() {
        List<Post> posts = postRepo.listHotPosts();
        List<FullInfoPost> list = posts.stream().map(post -> modelMapper.map(post, FullInfoPost.class)).toList();
        return list;
    }


    @Override
    public Long countPost() {
        return postRepo.count();
    }

    @Override
    public FullInfoPost getLatest() {
        return modelMapper.map(postRepo.findTopByOrderByCreatedAtDesc(), FullInfoPost.class);
    }

    @Override
    public List<FullInfoPost> listRecentPosts() {
        List<Post> recentList = postRepo.findFirst10ByOrderByCreatedAt();

        List<FullInfoPost> list = recentList.stream().map(item -> {
            FullInfoPost fullInfoPost = modelMapper.map(item, FullInfoPost.class);
            fullInfoPost.setNumOfComments(item.getComments().size());
            return fullInfoPost;
        }).toList();

        return list;
    }

    @Override
    public List<PostDTO> listTrashedPosts() {
        List<Post> postList = postRepo.findAllByTrashed();

        return postList.stream().map(post -> modelMapper.map(post, PostDTO.class)).toList();
    }

    @Override
    @Transactional
    public void updateTrashed(Long id, boolean trashed) throws ResourceNotFoundException {
        postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find any post with the given id: " + id));
        postRepo.updateEnabled(id,trashed);
    }



    private void checkUniqueSlug(String slug) {
        Optional<Post> postExisting = postRepo.findBySlug(slug);
        if (postExisting.isPresent()) {

            throw new DataIntegrityViolationException("Slug is already in use, please choose another title or slug");
        }
    }
}
