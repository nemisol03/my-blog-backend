package com.springboot.blog;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.Tag;
import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.payload.FullInfoPost;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.TagDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BlogApplication {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(Comment.class, CommentDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getPost().getId(), CommentDTO::setPostId));

//        modelMapper.typeMap(Tag.class, TagDTO.class)
//                .addMappings(mapper -> mapper.map(src -> src.ge);


        modelMapper.typeMap(Post.class, PostDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getTag().getId(),PostDTO::setTagId));
        modelMapper.typeMap(Post.class, PostDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getUser().getId(),PostDTO::setUserId));
//        modelMapper.typeMap(Post.class, FullInfoPost.class)
//                .addMappings(mapper -> mapper.map(src -> src.getComments()==null ?0: src.getComments().size(),FullInfoPost::setNumOfComments));


        return modelMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

}
