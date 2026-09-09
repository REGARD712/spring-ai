package com.me.chatbot.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
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
     * @param chatModel  Ollama的ChatModel
     * @param chatMemory 管理会话的ChatMemory
     * @return ChatClient Ollama的ChatClient
     * @Description: 封装OllamaChatClient(多轮对话, 有记忆功能)
     */
    @Bean
    public ChatClient ollamaChatClient (OllamaChatModel chatModel, ChatMemory chatMemory) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你叫贾维斯，是托尼斯塔克创造的人工智能助手，能够帮忙解决各种问题")
                .defaultAdvisors(new SimpleLoggerAdvisor(), new MessageChatMemoryAdvisor(chatMemory))
                .build();
    }

    /**
     * @param
     * @return ChatMemory 管理会话记忆的ChatMemory
     * @Description: 返回一个管理聊天会话记忆的ChatMemory
     */
    @Bean
    public ChatMemory chatMemory () {
        return new InMemoryChatMemory();
    }
}
