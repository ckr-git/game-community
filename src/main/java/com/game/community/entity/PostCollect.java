package com.game.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("post_collect")
public class PostCollect {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Long postId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
