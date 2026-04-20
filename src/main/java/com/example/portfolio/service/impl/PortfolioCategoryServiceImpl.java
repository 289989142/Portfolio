package com.example.portfolio.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.portfolio.entity.PortfolioCategory;
import com.example.portfolio.mapper.PortfolioCategoryMapper;
import com.example.portfolio.service.PortfolioCategoryService;
import org.springframework.stereotype.Service;

@Service
public class PortfolioCategoryServiceImpl extends ServiceImpl<PortfolioCategoryMapper, PortfolioCategory>
        implements PortfolioCategoryService {
}
