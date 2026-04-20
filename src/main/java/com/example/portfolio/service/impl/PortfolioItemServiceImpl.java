package com.example.portfolio.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.portfolio.entity.PortfolioItem;
import com.example.portfolio.mapper.PortfolioItemMapper;
import com.example.portfolio.service.PortfolioItemService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PortfolioItemServiceImpl extends ServiceImpl<PortfolioItemMapper, PortfolioItem>
        implements PortfolioItemService {

    @Override
    public List<PortfolioItem> listByCategoryId(Long categoryId) {
        if (categoryId == null) {
            return list();
        }
        return lambdaQuery()
                .eq(PortfolioItem::getCategoryId, categoryId)
                .orderByAsc(PortfolioItem::getSortOrder)
                .orderByDesc(PortfolioItem::getCreateTime)
                .list();
    }
}
