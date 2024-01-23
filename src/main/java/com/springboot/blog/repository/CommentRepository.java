package com.springboot.blog.repository;

import com.springboot.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("select c from Comment c left join fetch c.children where c.post.id = :postId and c.parent is null order by c.modifiedDate")
    List<Comment> listHiarchicalCommentsByPost(@Param("postId") Long postId);

}
