package com.game.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.game.community.entity.GameCollect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GameCollectMapper extends BaseMapper<GameCollect> {
    
    @Select("SELECT game_id FROM game_collect WHERE user_id = #{userId}")
    List<Long> selectGameIdsByUserId(@Param("userId") Long userId);
}
