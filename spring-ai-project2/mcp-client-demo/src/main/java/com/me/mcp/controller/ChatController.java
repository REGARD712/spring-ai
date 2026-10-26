package com.me.mcp.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MCP Chat Controller
 *
 * @ClassName: ChatController
 * @Author: JacksonLee
 * @Date: 2026/10/26
 */
@RequestMapping("/chat")
@RestController
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(DashScopeChatModel chatModel, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }

    /**
     * 生成回复
     *
     * @param message 用户提示词
     * @return String 模型回复
     */
    @RequestMapping("/call")
    public String call(String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }
}
