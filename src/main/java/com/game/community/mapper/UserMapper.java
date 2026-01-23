package com.game.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.game.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
