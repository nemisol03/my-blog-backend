package com.springboot.blog.utils;

import org.springframework.web.multipart.MultipartFile;

public class ImageValidator {
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public static void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds the maximum allowed size.It must be less than 5MB.");
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new IllegalArgumentException("Invalid image format");

        }
    }
}
