package com.game.community.common;

import com.game.community.BaseTest;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JWT工具类单元测试
 */
class JwtUtilTest extends BaseTest {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_USERNAME = "testuser";
    
    /**
     * FT-24: Token生成
     */
    @Test
    @DisplayName("Token生成测试")
    void testGenerateToken() {
        String token = jwtUtil.generateToken(TEST_USER_ID, TEST_USERNAME);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT格式: header.payload.signature
    }
    
    /**
     * FT-25: Token验证
     */
    @Test
    @DisplayName("Token验证测试")
    void testValidateToken() {
        String token = jwtUtil.generateToken(TEST_USER_ID, TEST_USERNAME);
        
        assertTrue(jwtUtil.validateToken(token));
    }
    
    /**
     * Token解析测试
     */
    @Test
    @DisplayName("Token解析测试")
    void testParseToken() {
        String token = jwtUtil.generateToken(TEST_USER_ID, TEST_USERNAME);
        
        Claims claims = jwtUtil.parseToken(token);
        
        assertNotNull(claims);
        assertEquals(TEST_USER_ID.toString(), claims.getSubject());
        assertEquals(TEST_USERNAME, claims.get("username"));
    }
    
    /**
     * 从Token获取用户ID测试
     */
    @Test
    @DisplayName("从Token获取用户ID测试")
    void testGetUserIdFromToken() {
        String token = jwtUtil.generateToken(TEST_USER_ID, TEST_USERNAME);
        
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        assertEquals(TEST_USER_ID, userId);
    }
    
    /**
     * FT-26: Token过期/无效测试
     */
    @Test
    @DisplayName("无效Token验证测试")
    void testInvalidToken() {
        String invalidToken = "invalid.token.string";
        
        assertFalse(jwtUtil.validateToken(invalidToken));
    }
    
    /**
     * 空Token验证测试
     */
    @Test
    @DisplayName("空Token验证测试")
    void testEmptyToken() {
        assertFalse(jwtUtil.validateToken(""));
        assertFalse(jwtUtil.validateToken(null));
    }
    
    /**
     * 篡改Token验证测试
     */
    @Test
    @DisplayName("篡改Token验证测试")
    void testTamperedToken() {
        String token = jwtUtil.generateToken(TEST_USER_ID, TEST_USERNAME);
        // 篡改token的最后一个字符
        String tamperedToken = token.substring(0, token.length() - 1) + "X";
        
        assertFalse(jwtUtil.validateToken(tamperedToken));
    }
}
