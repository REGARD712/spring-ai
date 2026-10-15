package com.me.alibaba.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.me.alibaba.tool.DateTimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Tool-Calling examples1
 *
 * @ClassName: ChatClientController
 * @Author: JacksonLee
 * @Date: 2026/10/14
 */
@RequestMapping("/chat")
@RestController
public class ChatClientController {
    private final ChatClient chatClient;

    public ChatClientController(DashScopeChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultTools(new DateTimeTools())
                .build();
    }

    @RequestMapping("/call")
    public String call (String message) {
        return chatClient
                .prompt()
                .user(message)
//                .tools(new DateTimeTool())
                .call()
                .content();
    }
}
