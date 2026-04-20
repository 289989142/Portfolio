CREATE TABLE IF NOT EXISTS site_settings (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    admin_password VARCHAR(255) NOT NULL COMMENT '管理员密码',
    bio_content TEXT COMMENT '站点简介',
    resume_url VARCHAR(512) DEFAULT NULL COMMENT '简历地址',
    phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    wechat_qr_url VARCHAR(512) DEFAULT NULL COMMENT '微信二维码地址',
    bg_image_url VARCHAR(512) DEFAULT NULL COMMENT '背景图地址',
    bg_music_url VARCHAR(512) DEFAULT NULL COMMENT '背景音乐地址',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='站点配置表';

CREATE TABLE IF NOT EXISTS portfolio_category (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    title VARCHAR(100) NOT NULL COMMENT '分类标题',
    cover_image_url VARCHAR(512) DEFAULT NULL COMMENT '封面图地址',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序值',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作品集分类表';

CREATE TABLE IF NOT EXISTS portfolio_item (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    media_url VARCHAR(512) NOT NULL COMMENT '作品媒体地址',
    description TEXT COMMENT '作品描述',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序值',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_category_id (category_id),
    KEY idx_sort_order (sort_order),
    CONSTRAINT fk_portfolio_item_category
        FOREIGN KEY (category_id) REFERENCES portfolio_category (id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作品集明细表';
