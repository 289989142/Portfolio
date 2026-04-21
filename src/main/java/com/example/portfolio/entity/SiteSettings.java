package com.example.portfolio.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("site_settings")
public class SiteSettings {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String adminPassword;

    private String bioContent;

    private String avatarUrl;

    private String resumeUrl;

    private String phone;

    private String wechatAccount;

    private String wechatQrUrl;

    private String bgImageUrl;

    private String bgMusicUrl;
}
