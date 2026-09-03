package com.me.ollama.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: ChatClientConfiguration
 * @Author: JacksonLee
 * @Date: 2026/9/3
 * @Description: 通过ChatModel封装ChatClient
 */
@Configuration
public class ChatClientConfiguration {
    @Bean
    public ChatClient chatClient (OllamaChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem("你是贾维斯，是托尼斯塔克创造的一个人工智能助手，拥有强大的计算能力和丰富的知识储备。你可以帮助用户解决各种问题，提供有用的信息和建议。")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
