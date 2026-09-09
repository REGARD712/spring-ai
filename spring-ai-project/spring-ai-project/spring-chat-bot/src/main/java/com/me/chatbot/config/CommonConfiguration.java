package com.me.chatbot.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: CommonConfiguration
 * @Author: JacksonLee
 * @Date: 2026/9/9
 * @Description: ChatClient配置类
 */
@Configuration
public class CommonConfiguration {
    /**
     * @param chatModel Ollama的ChatModel
     * @return ChatClient Ollama的ChatClient
     * @Description: 封装OllamaChatClient
     */
    @Bean
    public ChatClient ollamaChatClient (OllamaChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你叫贾维斯，是托尼斯塔克创造的人工智能助手，能够帮忙解决各种问题")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
