package com.game.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("chat_room")
public class ChatRoom {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private Long gameId;
    
    private String description;
    
    private String coverImage;
    
    private Integer memberCount;
    
    private Integer status;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
