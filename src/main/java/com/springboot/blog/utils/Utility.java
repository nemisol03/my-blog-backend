package com.springboot.blog.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Slf4j
public class Utility {

    public static String getSiteURL(HttpServletRequest servletRequest) {
        String url = servletRequest.getRequestURL().toString();
        log.info("url: "+ url);
        return url.replace(servletRequest.getServletPath(),"");
    }
    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settingBag) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(settingBag.getHost());
        mailSender.setPort(settingBag.getPort());
        mailSender.setUsername(settingBag.getUsername());
        mailSender.setPassword(settingBag.getPassword());

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth",settingBag.getSmtpAuth());
        properties.setProperty("mail.smtp.starttls.enable", settingBag.getSmtpSecured());
        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }


}
