package com.springboot.blog.repository;

import com.springboot.blog.entity.Setting;
import com.springboot.blog.entity.SettingCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting,Integer> {
    List<Setting> findByCategory(SettingCategory category);
}
