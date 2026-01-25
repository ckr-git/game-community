package com.game.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
@MapperScan("com.game.community.mapper")
public class CommunityPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityPlatformApplication.class, args);
        System.out.println("游戏社区交流平台启动成功！");
    }
}
