package com.springboot.blog.repository;

import com.springboot.blog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Long> {


    Tag findByName(String name);
}
