package com.example.portfolio.controller.publicapi;

import com.example.portfolio.common.Result;
import com.example.portfolio.entity.SiteSettings;
import com.example.portfolio.service.SiteSettingsService;
import com.example.portfolio.vo.SiteSettingsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicSettingsController {

    private final SiteSettingsService siteSettingsService;

    public PublicSettingsController(SiteSettingsService siteSettingsService) {
        this.siteSettingsService = siteSettingsService;
    }

    @GetMapping("/settings")
    public Result<SiteSettingsVO> getSettings() {
        SiteSettings settings = siteSettingsService.getSingletonSettings();
        SiteSettingsVO vo = new SiteSettingsVO();
        if (settings != null) {
            BeanUtils.copyProperties(settings, vo);
        }
        return Result.ok(vo);
    }
}
