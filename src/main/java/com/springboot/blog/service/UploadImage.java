package com.springboot.blog.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UploadImage {
    String upload(MultipartFile file,String folder);
}
