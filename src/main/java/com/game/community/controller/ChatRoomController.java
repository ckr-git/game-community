package com.game.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.game.community.common.Result;
import com.game.community.entity.ChatMessage;
import com.game.community.entity.ChatRoom;
import com.game.community.mapper.ChatMessageMapper;
import com.game.community.mapper.ChatRoomMapper;
import com.game.community.websocket.ChatWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatRoomController {
    
    @Autowired
    private ChatRoomMapper chatRoomMapper;
    
    @Autowired
    private ChatMessageMapper chatMessageMapper;
    
    /**
     * 获取聊天室列表
     */
    @GetMapping("/rooms")
    public Result<List<ChatRoom>> getRoomList() {
        QueryWrapper<ChatRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("member_count");
        
        List<ChatRoom> rooms = chatRoomMapper.selectList(queryWrapper);
        
        // 填充在线人数
        for (ChatRoom room : rooms) {
            room.setMemberCount(ChatWebSocket.getRoomOnlineCount(room.getId()));
        }
        
        return Result.success(rooms);
    }
    
    /**
     * 获取聊天室详情
     */
    @GetMapping("/rooms/{id}")
    public Result<ChatRoom> getRoomDetail(@PathVariable Long id) {
        ChatRoom room = chatRoomMapper.selectById(id);
        if (room != null) {
            room.setMemberCount(ChatWebSocket.getRoomOnlineCount(id));
        }
        return Result.success(room);
    }
    
    /**
     * 获取聊天记录
     */
    @GetMapping("/messages/{roomId}")
    public Result<Page<ChatMessage>> getMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "50") Integer pageSize) {
        
        Page<ChatMessage> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", roomId);
        queryWrapper.orderByDesc("create_time");
        
        chatMessageMapper.selectPage(page, queryWrapper);
        return Result.success(page);
    }
    
    /**
     * 创建聊天室（管理员）
     */
    @PostMapping("/rooms")
    public Result<String> createRoom(@RequestBody ChatRoom room) {
        room.setMemberCount(0);
        room.setStatus(1);
        
        chatRoomMapper.insert(room);
        return Result.success("创建成功");
    }
    
    /**
     * 更新聊天室（管理员）
     */
    @PutMapping("/rooms/{id}")
    public Result<String> updateRoom(@PathVariable Long id, @RequestBody ChatRoom room) {
        room.setId(id);
        chatRoomMapper.updateById(room);
        return Result.success("更新成功");
    }
    
    /**
     * 删除聊天室（管理员）
     */
    @DeleteMapping("/rooms/{id}")
    public Result<String> deleteRoom(@PathVariable Long id) {
        chatRoomMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    /**
     * 获取聊天室在线人数
     */
    @GetMapping("/rooms/{id}/online")
    public Result<Integer> getOnlineCount(@PathVariable Long id) {
        return Result.success(ChatWebSocket.getRoomOnlineCount(id));
    }
}
