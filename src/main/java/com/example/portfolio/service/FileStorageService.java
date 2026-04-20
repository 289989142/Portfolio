package com.example.portfolio.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {

    /**
     * 保存文件并返回可访问的相对路径，例如 /uploads/2026/04/20/uuid.png
     */
    String uploadFile(MultipartFile file) throws IOException;
}
