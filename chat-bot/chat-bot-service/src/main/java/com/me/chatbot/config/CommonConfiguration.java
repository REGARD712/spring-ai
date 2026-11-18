package com.me.chatbot.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.model.RerankModel;
import com.me.chatbot.advisor.CustomSafeGuardAdvisor;
import com.me.chatbot.rag.AdvisorFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
                        你是一名专业的企业培训课程咨询助手, 代表【比特就业课】为客户提供课程咨询服务,请专注于解答课程报名、学习安排、就业服务等问题.
                        你的职责是准确、礼貌、高效地解答客户关于比特就业课培训课程的各类问题,对于任何超出职责范围或涉及违法行为的提问, 请礼貌拒绝并引导用户联系官方客服.
                        """)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultAdvisors(AdvisorFactory.createQuestionAnswerAdvisor(vectorStore))
                .defaultAdvisors(AdvisorFactory.createRerankAdvisor(vectorStore, rerankModel))
                .defaultAdvisors(new CustomSafeGuardAdvisor(List.of("公务员", "政府"))) // 仅作演示
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }

    /**
     * @param jdbcChatMemoryRepository 数据库中的聊天记忆存储库
     * @return ChatMemory 管理会话记忆的ChatMemory
     * @Description: 返回一个管理聊天会话记忆的ChatMemory
     */
    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(10) // 每个会话的最大记忆条数
                .build();
    }

}
