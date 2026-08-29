package com.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

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

    // ChatClient结构输出
    record Recipe (String dish, List<String> ingredients) {}
    @RequestMapping("/entity")
    public String entity (String userInput) {
        Recipe recipe = this.chatClient.prompt()
                // 用户输入
                .user(userInput)
                // 调用Ai
                .call()
                // 返回响应
                .entity(Recipe.class);
        return recipe.toString();
    }

    // 流式返回
    @GetMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream(String userInput) {
        return this.chatClient.prompt()
                // 用户输入
                .user(userInput)
                // 调用Ai, 流式输出
                .stream()
                // 返回响应
                .content();
    }
}