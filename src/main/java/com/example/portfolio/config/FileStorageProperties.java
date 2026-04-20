package com.example.portfolio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app")
public class FileStorageProperties {

    /**
     * 本地上传根目录，例如 D:/uploads/ 或 /var/www/uploads/
     */
    private String uploadPath;

    /**
     * 对外访问前缀，例如 /uploads
     */
    private String uploadUrlPrefix = "/uploads";

    /**
     * 是否按日期创建子目录
     */
    private boolean uploadDateSubdir = true;
}
