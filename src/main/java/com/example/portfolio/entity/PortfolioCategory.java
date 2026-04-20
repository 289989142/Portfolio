package com.example.portfolio.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("portfolio_category")
public class PortfolioCategory {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;

    private String coverImageUrl;

    private Integer sortOrder;

    private LocalDateTime createTime;
}
