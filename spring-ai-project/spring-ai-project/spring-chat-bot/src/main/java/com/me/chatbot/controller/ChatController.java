package com.me.chatbot.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
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
    private final ChatClient ollamaChatClient;

    public ChatController(ChatClient ollamaChatClient) {
        this.ollamaChatClient = ollamaChatClient;
    }

    /**
     * @param prompt 用户输入的消息内容
     * @param chatId 会话的id, 用于实现会话的记忆
     * @return Flux<String> 流式返回的机器人回复内容
     * @Description: 与机器人对话（流式返回）
     */
    @RequestMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream (@RequestParam("prompt") String prompt,
                                @RequestParam("chatId") String chatId) {
        return this.ollamaChatClient.prompt()
                .user(prompt)
                .advisors(spec -> spec.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content();
    }
}
