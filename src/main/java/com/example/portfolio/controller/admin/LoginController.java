package com.example.portfolio.controller.admin;

import com.example.portfolio.common.Result;
import com.example.portfolio.dto.LoginRequest;
import com.example.portfolio.entity.SiteSettings;
import com.example.portfolio.service.SiteSettingsService;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class LoginController {

    private final SiteSettingsService siteSettingsService;

    public LoginController(SiteSettingsService siteSettingsService) {
        this.siteSettingsService = siteSettingsService;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        if (request == null || !StringUtils.hasText(request.getPassword())) {
            throw new IllegalArgumentException("密码不能为空");
        }

        SiteSettings settings = siteSettingsService.getSingletonSettings();
        if (settings == null || !StringUtils.hasText(settings.getAdminPassword())) {
            throw new IllegalArgumentException("站点管理员密码未配置");
        }
        if (!request.getPassword().equals(settings.getAdminPassword())) {
            throw new IllegalArgumentException("密码错误");
        }

        LoginResponse response = new LoginResponse();
        response.setLoggedIn(true);
        response.setMessage("登录成功");
        return Result.ok("登录成功", response);
    }

    @Data
    public static class LoginResponse {
        private boolean loggedIn;
        private String message;
    }
}
