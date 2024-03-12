package com.springboot.blog.controller;

import com.springboot.blog.Constants;
import com.springboot.blog.entity.Setting;
import com.springboot.blog.entity.SettingBag;
import com.springboot.blog.payload.MailSettingDTO;
import com.springboot.blog.payload.MailTemplateDTO;
import com.springboot.blog.service.SettingService;
import com.springboot.blog.utils.EmailSettingBag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
public class SettingController {
    private final SettingService settingService;


    @GetMapping("/mail")
    public ResponseEntity<?> listMailSettings() {
        EmailSettingBag emailSettingBag = settingService.listMailSettings();
        if (emailSettingBag.list().size() > 0) {
            return ResponseEntity.ok().body(emailSettingBag.list());
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/mail-server")
    public ResponseEntity<?> saveMailServerSetting(@RequestBody MailSettingDTO mailSettingDTO) {
        EmailSettingBag emailSettingBag = settingService.listMailSettings();
        SettingBag settingBag = new SettingBag(emailSettingBag.list());
        saveMailServerConfig(settingBag, mailSettingDTO);


//       saveInfoInForm(request,settingBag, emailSettingBag.list());

//        settingService.updateSetting(settings);

        return ResponseEntity.ok().body("Mail server info has been updated successfully!");
    }

    @PatchMapping("/mail-template")
    public ResponseEntity<?> saveMailTemplateSetting(@RequestBody MailTemplateDTO mailTemplateDTO) {
        EmailSettingBag emailSettingBag = settingService.listMailSettings();
        SettingBag settingBag = new SettingBag(emailSettingBag.list());
        saveMailTemplateConfig(settingBag, mailTemplateDTO);


//       saveInfoInForm(request,settingBag, emailSettingBag.list());

//        settingService.updateSetting(settings);

        return ResponseEntity.ok().body("Mail server info has been updated successfully!");
    }

    private void saveMailTemplateConfig(SettingBag settingBag, MailTemplateDTO mailTemplateDTO) {
        settingBag.updateValue(Constants.CUSTOMER_VERIFY_ACC_SUBJECT, mailTemplateDTO.getSubject());
        settingBag.updateValue(Constants.CUSTOMER_VERIFY_ACC_CONTENT, String.valueOf(mailTemplateDTO.getContent()));
        settingService.updateSetting(settingBag.list());

    }


    private void saveMailServerConfig( SettingBag settingBag, MailSettingDTO mailSetting) {
            settingBag.updateValue(Constants.MAIL_HOST, mailSetting.getHost());
            settingBag.updateValue(Constants.MAIL_PORT, String.valueOf(mailSetting.getPort()));
            settingBag.updateValue(Constants.MAIL_USERNAME, mailSetting.getUsername());
            settingBag.updateValue(Constants.MAIL_PASSWORD, mailSetting.getPassword());
            settingBag.updateValue(Constants.MAIL_SENDER_NAME, mailSetting.getSenderName());
            settingBag.updateValue(Constants.MAIL_AUTH, mailSetting.getSmtpAuth());
            settingBag.updateValue(Constants.MAIL_SECURED, mailSetting.getSmtpSecured());
            settingBag.updateValue(Constants.MAIL_FROM, mailSetting.getMailFrom());
            settingService.updateSetting(settingBag.list());

    }

}
