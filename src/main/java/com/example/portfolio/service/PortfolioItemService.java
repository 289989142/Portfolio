package com.example.portfolio.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.portfolio.entity.PortfolioItem;

import java.util.List;

public interface PortfolioItemService extends IService<PortfolioItem> {

    List<PortfolioItem> listByCategoryId(Long categoryId);
}
