package com.game.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_like")
public class UserLike {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Integer targetType; // 目标类型：1-游戏 2-资讯 3-帖子 4-评论
    
    private Long targetId;
    
    private Long userId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
