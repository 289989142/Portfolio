package com.example.portfolio.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.portfolio.entity.SiteSettings;

public interface SiteSettingsService extends IService<SiteSettings> {

    SiteSettings getSingletonSettings();

    SiteSettings saveOrUpdateSingleton(SiteSettings settings);
}
