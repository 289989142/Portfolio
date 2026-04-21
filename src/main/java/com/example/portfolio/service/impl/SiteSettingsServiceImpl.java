package com.example.portfolio.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.portfolio.entity.SiteSettings;
import com.example.portfolio.mapper.SiteSettingsMapper;
import com.example.portfolio.service.SiteSettingsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
public class SiteSettingsServiceImpl extends ServiceImpl<SiteSettingsMapper, SiteSettings> implements SiteSettingsService {

    @Override
    public SiteSettings getSingletonSettings() {
        return lambdaQuery().orderByAsc(SiteSettings::getId).last("LIMIT 1").one();
    }

    @Override
    public SiteSettings saveOrUpdateSingleton(SiteSettings settings) {
        SiteSettings current = getSingletonSettings();
        if (current == null) {
            SiteSettings toSave = new SiteSettings();
            copySettings(settings, toSave);
            save(toSave);
            return toSave;
        }
        copySettings(settings, current);
        updateById(current);
        return current;
    }

    private void copySettings(SiteSettings source, SiteSettings target) {
        if (source == null || target == null) {
            return;
        }
        if (StringUtils.hasText(source.getAdminPassword())) {
            target.setAdminPassword(source.getAdminPassword());
        }
        target.setBioContent(source.getBioContent());
        target.setAvatarUrl(source.getAvatarUrl());
        target.setResumeUrl(source.getResumeUrl());
        target.setPhone(source.getPhone());
        target.setWechatAccount(source.getWechatAccount());
        target.setWechatQrUrl(source.getWechatQrUrl());
        target.setBgImageUrl(source.getBgImageUrl());
        target.setBgMusicUrl(source.getBgMusicUrl());
    }
}
