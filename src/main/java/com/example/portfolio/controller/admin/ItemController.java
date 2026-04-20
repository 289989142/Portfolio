package com.example.portfolio.controller.admin;

import com.example.portfolio.common.Result;
import com.example.portfolio.entity.PortfolioCategory;
import com.example.portfolio.entity.PortfolioItem;
import com.example.portfolio.service.PortfolioCategoryService;
import com.example.portfolio.service.PortfolioItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/items")
public class ItemController {

    private final PortfolioItemService itemService;
    private final PortfolioCategoryService categoryService;

    public ItemController(PortfolioItemService itemService, PortfolioCategoryService categoryService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public Result<List<PortfolioItem>> list(@RequestParam(required = false) Long categoryId) {
        return Result.ok(itemService.listByCategoryId(categoryId));
    }

    @GetMapping("/{id}")
    public Result<PortfolioItem> detail(@PathVariable Long id) {
        PortfolioItem item = itemService.getById(id);
        if (item == null) {
            throw new IllegalArgumentException("作品不存在");
        }
        return Result.ok(item);
    }

    @PostMapping
    public Result<PortfolioItem> create(@RequestBody PortfolioItem request) {
        validateRequest(request);
        PortfolioCategory category = categoryService.getById(request.getCategoryId());
        if (category == null) {
            throw new IllegalArgumentException("分类不存在");
        }
        PortfolioItem item = new PortfolioItem();
        BeanUtils.copyProperties(request, item, "id", "createTime");
        item.setCreateTime(LocalDateTime.now());
        itemService.save(item);
        return Result.ok("创建成功", item);
    }

    @PutMapping
    public Result<PortfolioItem> update(@RequestBody PortfolioItem request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("id 不能为空");
        }
        validateRequest(request);
        PortfolioItem item = itemService.getById(request.getId());
        if (item == null) {
            throw new IllegalArgumentException("作品不存在");
        }
        PortfolioCategory category = categoryService.getById(request.getCategoryId());
        if (category == null) {
            throw new IllegalArgumentException("分类不存在");
        }
        BeanUtils.copyProperties(request, item, "id", "createTime");
        itemService.updateById(item);
        return Result.ok("更新成功", item);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean removed = itemService.removeById(id);
        return Result.ok("删除成功", removed);
    }

    private void validateRequest(PortfolioItem request) {
        if (request == null) {
            throw new IllegalArgumentException("请求体不能为空");
        }
        if (request.getCategoryId() == null) {
            throw new IllegalArgumentException("categoryId 不能为空");
        }
        if (!StringUtils.hasText(request.getMediaUrl())) {
            throw new IllegalArgumentException("mediaUrl 不能为空");
        }
    }
}
