package com.ai.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: ChatClientConfiguration
 * @Author: JacksonLee
 * @Date: 2026/8/28
 * @Description: ChatClient聊天模型配置
 */
@Configuration
public class ChatClientConfiguration {
    @Bean
    public ChatClient ChatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                // 设置系统提示词
                .defaultSystem("你叫贾维斯，你是一个由钢铁侠（托尼·斯塔克）创造的AI助手，你可以帮助处理日常事情")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
