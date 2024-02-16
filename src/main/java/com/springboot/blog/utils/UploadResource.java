package com.springboot.blog.utils;

import com.springboot.blog.payload.ImageInfo;
import com.springboot.blog.service.UploadImage;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UploadResource {

    private final UploadImage cloudinaryService;


    public String uploadImage(MultipartFile file, String folder) {
            ImageValidator.validateImage(file);
            String url = this.cloudinaryService.upload(file, folder);
        return url;
    }
}
