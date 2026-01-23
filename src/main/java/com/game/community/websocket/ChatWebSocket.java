package com.game.community.websocket;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/websocket/chat/{roomId}/{userId}")
public class ChatWebSocket {
    
    // 存储每个聊天室的连接
    private static Map<Long, CopyOnWriteArraySet<ChatWebSocket>> roomClients = new ConcurrentHashMap<>();
    
    private Session session;
    private Long roomId;
    private Long userId;
    
    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") Long roomId, @PathParam("userId") Long userId) {
        this.session = session;
        this.roomId = roomId;
        this.userId = userId;
        
        // 添加到对应的聊天室
        roomClients.computeIfAbsent(roomId, k -> new CopyOnWriteArraySet<>()).add(this);
        
        log.info("用户{}加入聊天室{}", userId, roomId);
        
        // 发送系统消息通知其他用户
        JSONObject message = new JSONObject();
        message.set("type", "system");
        message.set("content", "用户" + userId + "加入了聊天室");
        message.set("roomId", roomId);
        sendMessageToRoom(roomId, message.toString());
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到用户{}在聊天室{}的消息: {}", userId, roomId, message);
        
        try {
            JSONObject jsonMessage = JSONUtil.parseObj(message);
            jsonMessage.set("userId", userId);
            jsonMessage.set("roomId", roomId);
            jsonMessage.set("timestamp", System.currentTimeMillis());
            
            // 广播消息到聊天室所有用户
            sendMessageToRoom(roomId, jsonMessage.toString());
        } catch (Exception e) {
            log.error("消息处理失败", e);
        }
    }
    
    @OnClose
    public void onClose() {
        CopyOnWriteArraySet<ChatWebSocket> clients = roomClients.get(roomId);
        if (clients != null) {
            clients.remove(this);
            if (clients.isEmpty()) {
                roomClients.remove(roomId);
            }
        }
        
        log.info("用户{}离开聊天室{}", userId, roomId);
        
        // 发送系统消息通知其他用户
        JSONObject message = new JSONObject();
        message.set("type", "system");
        message.set("content", "用户" + userId + "离开了聊天室");
        message.set("roomId", roomId);
        sendMessageToRoom(roomId, message.toString());
    }
    
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket错误", error);
    }
    
    /**
     * 发送消息到指定聊天室
     */
    private void sendMessageToRoom(Long roomId, String message) {
        CopyOnWriteArraySet<ChatWebSocket> clients = roomClients.get(roomId);
        if (clients != null) {
            for (ChatWebSocket client : clients) {
                try {
                    client.session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    log.error("发送消息失败", e);
                }
            }
        }
    }
    
    /**
     * 获取聊天室在线人数
     */
    public static int getRoomOnlineCount(Long roomId) {
        CopyOnWriteArraySet<ChatWebSocket> clients = roomClients.get(roomId);
        return clients != null ? clients.size() : 0;
    }
}
