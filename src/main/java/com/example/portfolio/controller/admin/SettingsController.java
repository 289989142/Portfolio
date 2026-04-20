package com.example.portfolio.controller.admin;

import com.example.portfolio.common.Result;
import com.example.portfolio.dto.SiteSettingsUpdateRequest;
import com.example.portfolio.entity.SiteSettings;
import com.example.portfolio.service.SiteSettingsService;
import com.example.portfolio.vo.SiteSettingsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/settings")
public class SettingsController {

    private final SiteSettingsService siteSettingsService;

    public SettingsController(SiteSettingsService siteSettingsService) {
        this.siteSettingsService = siteSettingsService;
    }

    @GetMapping
    public Result<SiteSettingsVO> getSettings() {
        SiteSettings settings = siteSettingsService.getSingletonSettings();
        SiteSettingsVO vo = new SiteSettingsVO();
        if (settings != null) {
            BeanUtils.copyProperties(settings, vo);
        }
        return Result.ok(vo);
    }

    @PutMapping
    public Result<SiteSettingsVO> updateSettings(@RequestBody SiteSettingsUpdateRequest request) {
        SiteSettings settings = new SiteSettings();
        BeanUtils.copyProperties(request, settings);
        SiteSettings saved = siteSettingsService.saveOrUpdateSingleton(settings);

        SiteSettingsVO vo = new SiteSettingsVO();
        BeanUtils.copyProperties(saved, vo);
        return Result.ok("更新成功", vo);
    }
}
