package com.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: ChatClientController
 * @Author: JacksonLee
 * @Date: 2026/8/28
 * @Description: SpringAi聊天模型
 */
@RequestMapping("/chat")
@RestController
public class ChatClientController {
    @Autowired
    private ChatClient chatClient;

    @GetMapping("/call")
    public String generation(String userInput) {
        return this.chatClient.prompt()
                // 用户输入
                .user(userInput)
                // 调用Ai
                .call()
                // 返回响应
                .content();
    }
}
