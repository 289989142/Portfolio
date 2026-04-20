package com.example.portfolio.service.impl;

import com.example.portfolio.config.FileStorageProperties;
import com.example.portfolio.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final FileStorageProperties properties;

    public LocalFileStorageService(FileStorageProperties properties) {
        this.properties = properties;
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        String uploadPath = properties.getUploadPath();
        if (!StringUtils.hasText(uploadPath)) {
            throw new IllegalStateException("app.upload-path 未配置");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String uniqueName = UUID.randomUUID().toString().replace("-", "");
        if (StringUtils.hasText(extension)) {
            uniqueName = uniqueName + "." + extension;
        }

        String relativeDir = "";
        if (properties.isUploadDateSubdir()) {
            relativeDir = LocalDate.now().toString().replace("-", "/");
        }

        Path basePath = Paths.get(uploadPath).toAbsolutePath().normalize();
        Path targetDir = StringUtils.hasText(relativeDir) ? basePath.resolve(relativeDir) : basePath;
        Files.createDirectories(targetDir);

        Path targetFile = targetDir.resolve(uniqueName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetFile);
        }

        StringBuilder url = new StringBuilder();
        url.append(normalizePrefix(properties.getUploadUrlPrefix()));
        if (StringUtils.hasText(relativeDir)) {
            url.append("/").append(relativeDir);
        }
        url.append("/").append(uniqueName);
        return url.toString().replace("\\", "/");
    }

    private String normalizePrefix(String prefix) {
        if (!StringUtils.hasText(prefix)) {
            return "/uploads";
        }
        String normalized = prefix.trim().replace("\\", "/");
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        return normalized.endsWith("/") ? normalized.substring(0, normalized.length() - 1) : normalized;
    }
}
