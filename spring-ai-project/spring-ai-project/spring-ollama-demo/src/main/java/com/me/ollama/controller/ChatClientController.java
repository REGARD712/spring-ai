package com.me.ollama.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @ClassName: ChatClientController
 * @Author: JacksonLee
 * @Date: 2026/9/3
 * @Description: ChatClient调用本地部署的DeepSeek
 */
@RequestMapping("/chat")
@RestController
public class ChatClientController {
    private final ChatClient chatClient;

    public ChatClientController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @RequestMapping("/chat")
    public String chat (String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @RequestMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream (String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }
}
