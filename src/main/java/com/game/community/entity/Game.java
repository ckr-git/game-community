package com.game.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("game")
public class Game {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    
    private String coverImage;
    
    private String description;
    
    private Long categoryId;
    
    private String tags;
    
    private String developer;
    
    private LocalDate releaseDate;
    
    private String downloadUrl;
    
    private Integer downloadCount;
    
    private Integer viewCount;
    
    private Integer likeCount;
    
    private Integer collectCount;
    
    private Integer status;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
