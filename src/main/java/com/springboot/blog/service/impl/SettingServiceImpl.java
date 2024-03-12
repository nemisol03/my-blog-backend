package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Setting;
import com.springboot.blog.entity.SettingCategory;
import com.springboot.blog.repository.SettingRepository;
import com.springboot.blog.service.SettingService;
import com.springboot.blog.utils.EmailSettingBag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepo;
    @Override
    public EmailSettingBag listMailSettings() {
        List<Setting> mailsSetting = settingRepo.findByCategory(SettingCategory.MAIL_SERVER);
        mailsSetting.addAll(settingRepo.findByCategory(SettingCategory.MAIL_TEMPLATE));
        return new EmailSettingBag(mailsSetting);
    }

    @Override
    public void updateSetting(List<Setting> settings) {
        settingRepo.saveAll(settings);
    }
}
