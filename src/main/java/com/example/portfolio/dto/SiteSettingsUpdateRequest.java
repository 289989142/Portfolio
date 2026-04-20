package com.example.portfolio.dto;

import lombok.Data;

@Data
public class SiteSettingsUpdateRequest {

    private String adminPassword;
    private String bioContent;
    private String resumeUrl;
    private String phone;
    private String wechatQrUrl;
    private String bgImageUrl;
    private String bgMusicUrl;
}
