package com.springboot.blog.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.springboot.blog.service.UploadImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UploadImageImpl implements UploadImage {

    private final Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile file, String folder) {
        try {
            Map<String, Object> options = new HashMap<>();
            if (folder != null && !folder.isEmpty()) {
                options.put("folder",  folder);
            }
            // generate Unique file
            options.put("public_id", UUID.randomUUID()+"_"+System.currentTimeMillis());
            Map response = cloudinary.uploader().upload(file.getBytes(), options);
            return response.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
        }
    }
}
