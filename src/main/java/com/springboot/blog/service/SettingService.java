package com.springboot.blog.service;

import com.springboot.blog.entity.Setting;
import com.springboot.blog.utils.EmailSettingBag;

import java.util.List;

public interface SettingService {
    EmailSettingBag listMailSettings();

    void updateSetting(List<Setting> settings);
}
