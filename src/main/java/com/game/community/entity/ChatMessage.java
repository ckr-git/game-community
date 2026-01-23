package com.game.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("chat_message")
public class ChatMessage {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long roomId;
    
    private Long userId;
    
    private String content;
    
    private Integer messageType;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
