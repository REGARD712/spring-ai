package com.me.chatbot.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @ClassName: ChatController
 * @Author: JacksonLee
 * @Date: 2026/9/9
 * @Description: 简单对话相关接口
 */
@RequestMapping("/chat")
@RestController
public class ChatController {
    @Resource
    private ChatClient ollamaChatClient;

    /**
     * @param prompt 用户输入的消息内容
     * @return Flux<String> 流式返回的机器人回复内容
     * @Description: 与机器人对话（流式返回）
     */
    @RequestMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream (@RequestParam("prompt") String prompt) {
        return this.ollamaChatClient.prompt()
                .user(prompt)
                .stream()
                .content();
    }
}
