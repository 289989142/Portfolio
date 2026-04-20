package com.example.portfolio.controller.publicapi;

import com.example.portfolio.common.Result;
import com.example.portfolio.entity.PortfolioCategory;
import com.example.portfolio.entity.PortfolioItem;
import com.example.portfolio.service.PortfolioCategoryService;
import com.example.portfolio.service.PortfolioItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicCategoryController {

    private final PortfolioCategoryService categoryService;
    private final PortfolioItemService itemService;

    public PublicCategoryController(PortfolioCategoryService categoryService, PortfolioItemService itemService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
    }

    @GetMapping("/categories")
    public Result<List<PortfolioCategory>> listCategories() {
        return Result.ok(categoryService.lambdaQuery()
                .orderByAsc(PortfolioCategory::getSortOrder)
                .orderByDesc(PortfolioCategory::getCreateTime)
                .list());
    }

    @GetMapping("/categories/{id}/items")
    public Result<List<PortfolioItem>> listItemsByCategory(@PathVariable Long id) {
        return Result.ok(itemService.listByCategoryId(id));
    }
}
