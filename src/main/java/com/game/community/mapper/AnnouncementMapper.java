package com.game.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.game.community.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {
}
