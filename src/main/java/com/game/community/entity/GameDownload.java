package com.game.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("game_download")
public class GameDownload {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Long gameId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
