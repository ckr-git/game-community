package com.game.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.game.community.entity.News;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
}
