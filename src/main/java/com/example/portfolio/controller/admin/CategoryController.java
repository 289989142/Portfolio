package com.example.portfolio.controller.admin;

import com.example.portfolio.common.Result;
import com.example.portfolio.entity.PortfolioCategory;
import com.example.portfolio.service.PortfolioCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {

    private final PortfolioCategoryService categoryService;

    public CategoryController(PortfolioCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Result<List<PortfolioCategory>> list() {
        return Result.ok(categoryService.lambdaQuery()
                .orderByAsc(PortfolioCategory::getSortOrder)
                .orderByDesc(PortfolioCategory::getCreateTime)
                .list());
    }

    @GetMapping("/{id}")
    public Result<PortfolioCategory> detail(@PathVariable Long id) {
        PortfolioCategory category = categoryService.getById(id);
        if (category == null) {
            throw new IllegalArgumentException("分类不存在");
        }
        return Result.ok(category);
    }

    @PostMapping
    public Result<PortfolioCategory> create(@RequestBody PortfolioCategory request) {
        if (!StringUtils.hasText(request.getTitle())) {
            throw new IllegalArgumentException("分类标题不能为空");
        }
        PortfolioCategory category = new PortfolioCategory();
        BeanUtils.copyProperties(request, category, "id", "createTime");
        category.setCreateTime(LocalDateTime.now());
        categoryService.save(category);
        return Result.ok("创建成功", category);
    }

    @PutMapping
    public Result<PortfolioCategory> update(@RequestBody PortfolioCategory request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("id 不能为空");
        }
        PortfolioCategory category = categoryService.getById(request.getId());
        if (category == null) {
            throw new IllegalArgumentException("分类不存在");
        }
        BeanUtils.copyProperties(request, category, "id", "createTime");
        categoryService.updateById(category);
        return Result.ok("更新成功", category);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean removed = categoryService.removeById(id);
        return Result.ok("删除成功", removed);
    }
}
