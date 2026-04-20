package com.example.portfolio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.portfolio.entity.PortfolioItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PortfolioItemMapper extends BaseMapper<PortfolioItem> {
}
