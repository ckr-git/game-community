package com.game.community.common;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Result结果封装类单元测试
 */
class ResultTest {
    
    /**
     * 成功结果测试 - 无数据
     */
    @Test
    @DisplayName("成功结果测试-无数据")
    void testSuccessWithoutData() {
        Result<Void> result = Result.success();
        
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertNull(result.getData());
    }
    
    /**
     * 成功结果测试 - 有数据
     */
    @Test
    @DisplayName("成功结果测试-有数据")
    void testSuccessWithData() {
        String testData = "测试数据";
        Result<String> result = Result.success(testData);
        
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertEquals(testData, result.getData());
    }
    
    /**
     * 错误结果测试 - 仅消息
     */
    @Test
    @DisplayName("错误结果测试-仅消息")
    void testErrorWithMessage() {
        String errorMsg = "操作失败";
        Result<Void> result = Result.error(errorMsg);
        
        assertEquals(500, result.getCode());
        assertEquals(errorMsg, result.getMessage());
        assertNull(result.getData());
    }
    
    /**
     * 错误结果测试 - 自定义错误码
     */
    @Test
    @DisplayName("错误结果测试-自定义错误码")
    void testErrorWithCodeAndMessage() {
        int errorCode = 401;
        String errorMsg = "未授权";
        Result<Void> result = Result.error(errorCode, errorMsg);
        
        assertEquals(errorCode, result.getCode());
        assertEquals(errorMsg, result.getMessage());
        assertNull(result.getData());
    }
    
    /**
     * 泛型数据类型测试
     */
    @Test
    @DisplayName("泛型数据类型测试")
    void testGenericType() {
        // 测试整数类型
        Result<Integer> intResult = Result.success(100);
        assertEquals(Integer.class, intResult.getData().getClass());
        
        // 测试对象类型
        TestObject obj = new TestObject("test");
        Result<TestObject> objResult = Result.success(obj);
        assertEquals(TestObject.class, objResult.getData().getClass());
        assertEquals("test", objResult.getData().getName());
    }
    
    // 测试用内部类
    static class TestObject {
        private String name;
        
        TestObject(String name) {
            this.name = name;
        }
        
        String getName() {
            return name;
        }
    }
}
