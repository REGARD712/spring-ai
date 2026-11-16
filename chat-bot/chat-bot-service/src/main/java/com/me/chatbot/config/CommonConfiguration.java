package com.me.chatbot.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.model.RerankModel;
import com.me.chatbot.rag.AdvisorFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
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
     * 封装DashScopeChatClient
     *
     * @param chatModel   DashScope的ChatModel
     * @param chatMemory  管理会话记忆的ChatMemory
     * @param vectorStore 向量数据库
     * @param rerankModel 重排序模型
     * @return ChatClient 配置好的ChatClient
     */
    @Bean
     public ChatClient dashScopeChatClient(
            DashScopeChatModel chatModel,
            ChatMemory chatMemory,
            VectorStore vectorStore,
            RerankModel rerankModel,
            ToolCallbackProvider toolCallbackProvider
    ) {
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        你是一名专业的企业培训课程咨询助手, 代表【比特就业课】为客户提供课程咨询服务.
                        你的职责是准确、礼貌、高效地解答客户关于比特就业课培训课程的各类问题
                        """)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultAdvisors(AdvisorFactory.createQuestionAnswerAdvisor(vectorStore))
                .defaultAdvisors(AdvisorFactory.createRerankAdvisor(vectorStore, rerankModel))
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }

    /**
     * @param
     * @return ChatMemory 管理会话记忆的ChatMemory
     * @Description: 返回一个管理聊天会话记忆的ChatMemory
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(10) // 最大记忆条数
                .build();
    }
}
