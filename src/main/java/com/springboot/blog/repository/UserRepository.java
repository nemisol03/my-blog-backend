package com.springboot.blog.repository;

import com.springboot.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u WHERE u.trashed = false")

    Page<User> findAllUserNotTrashed(Pageable pageable);

    @Query("select u from User  u where concat(u.firstName,' ',u.lastName,' ',u.email) like %?1%")
    Page<User> search(String keyword, Pageable pageable);

    Optional<User> findByEmail(String email);

    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    void updateEnabled(Long id,boolean enabled);

    @Query("SELECT u FROM User u WHERE u.trashed = true")
    List<User> listTrashedUsers();

    @Query("UPDATE User u SET u.trashed = ?2 WHERE u.id = ?1")
    @Modifying
    void updateTrashed(Long id, boolean trashed);


    @Query("UPDATE User u SET u.mfaEnabled  = ?2 where u.email = ?1")
    @Modifying
    void switchTFAstatus(String email,boolean enabled);
}
