package com.game.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("report")
public class Report {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Integer targetType; // 举报类型：1-帖子 2-评论 3-用户
    
    private Long targetId;
    
    private Long reporterId;
    
    private String reason;
    
    private String description;
    
    private Integer status; // 状态：0-待处理 1-已处理 2-已驳回
    
    private Long handlerId;
    
    private String handleResult;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
