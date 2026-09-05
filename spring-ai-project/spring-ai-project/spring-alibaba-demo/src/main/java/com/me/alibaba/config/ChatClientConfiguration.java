package com.me.alibaba.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: ChatClientConfiguration
 * @Author: JacksonLee
 * @Date: 2026/9/5
 * @Description: ChatClient默认系统配置
 */
@Configuration
public class ChatClientConfiguration {
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem("你是贾维斯，由托尼斯塔克创造的人工智能助手，擅长回答各种问题。在回答之前，先发送：{word}")
                .build();
    }
}
