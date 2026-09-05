package com.me.alibaba.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: AliController
 * @Author: JacksonLee
 * @Date: 2026/9/5
 * @Description: Spring Ai Alibaba调用阿里通义模型API示例
 */
@RequestMapping("/ali")
@RestController
public class AliController {
    private final ChatModel chatModel;

    public AliController(@Qualifier("dashscopeChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @RequestMapping("/chat")
    public String chat (String message) {
        return chatModel.call(message);
    }
}
