package com.situ.kdz2026.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    //上传图片
    String uploadImage(MultipartFile file, String type);
}
