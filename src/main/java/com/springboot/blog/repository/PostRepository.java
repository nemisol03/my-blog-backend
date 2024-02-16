package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import com.springboot.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p from Post p where p.status = 'APPROVED' and p.trashed=false and p.slug= ?1")
    Optional<Post> findBySlug(String slug);

    @Query("select p from Post  p where concat(p.title,' ',p.content) like %?1%")
    Page<Post> search(String keyword, Pageable pageable);



    @Query("SELECT p FROM Post p WHERE p.trashed = false AND p.status = 'APPROVED'")
    Page<Post> findAllApprovedPosts(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.trashed = false ")
    Page<Post> findAllNotTrashedPosts(Pageable pageable);

    @Query("UPDATE Post p SET p.trashed = ?2 WHERE p.id = ?1")
    @Modifying
    void updateTrashed(Long id,boolean trashed);


    @Query("SELECT p FROM Post p WHERE p.tag.id = ?1")
    List<Post> findAllByTagId(Long id);

    @Query("SELECT p from Post p where p.status = 'APPROVED' and p.trashed=false and p.id= ?1")
    Optional<Post> findByIdBoundary(Long id);

//    @Query("SELECT p FROM Post p WHERE p.status= 'APPROVED' ORDER BY p.createdAt DESC")
//    Post getLatest(Pageable pageable);

    @Query("SELECT p from Post p WHERE p.trashed = false AND p.status='APPROVED' ORDER BY p.createdAt DESC LIMIT 1")
    Post findTopByOrderByCreatedAtDesc();
//    List<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Post> findFirst10ByOrderByCreatedAt();

    @Query("SELECT p FROM Post p WHERE p.trashed = true")
    List<Post> findAllByTrashed();

    @Query("UPDATE Post p SET p.trashed = ?2 WHERE p.id = ?1")
    @Modifying
    void updateEnabled(Long id, boolean trashed);

    @Query("SELECT p FROM Post p WHERE p.tag.id = ?2 AND p.id<>?1 AND p.status='APPROVED'")
    List<Post> listRelatedPosts(Long id,Long tagId);

    @Query("SELECT p FROM Post p WHERE p.hot = true AND p.status='APPROVED'")
    List<Post> listHotPosts();

    Long countByUserId(Long userId);

}
