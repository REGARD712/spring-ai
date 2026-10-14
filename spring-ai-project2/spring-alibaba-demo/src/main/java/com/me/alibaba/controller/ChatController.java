package com.me.alibaba.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.me.alibaba.tool.DateTimeTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Tool-Calling examples
 *
 * @ClassName: ChatController
 * @Author: JacksonLee
 * @Date: 2026/10/14
 */
@RequestMapping("/chat")
@RestController
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(DashScopeChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    @RequestMapping("/call")
    public String call (String message) {
        return chatClient
                .prompt()
                .user(message)
                .tools(new DateTimeTool())
                .call()
                .content();
    }
}
