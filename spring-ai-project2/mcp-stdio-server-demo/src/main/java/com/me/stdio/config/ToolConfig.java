package com.me.stdio.config;

import com.me.stdio.service.UserService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP 工具配置类
 *
 * @ClassName: ToolConfig
 * @Author: JacksonLee
 * @Date: 2026/10/26
 */
@Configuration
public class ToolConfig {

    /**
     * 暴露 UserService 服务类的方法作为工具
     *
     * @param userService
     * @return ToolCallbackProvider
     */
    @Bean
    public ToolCallbackProvider getUserInfo(UserService userService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(userService)
                .build();
    }
}
