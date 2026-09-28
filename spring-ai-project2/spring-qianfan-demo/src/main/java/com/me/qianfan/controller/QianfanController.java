package com.me.qianfan.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: QianfanController
 * @Author: JacksonLee
 * @Date: 2026/9/17
 * @Description: Qianfan配置
 */
@RequestMapping("/qianfan")
@RestController
public class QianfanController {
    @Resource
    private OpenAiChatModel openAiChatModel;

    @Resource
    private OpenAiImageModel openAiImageModel;

    @RequestMapping("/chat")
    public String chat (String message) {
        return openAiChatModel.call(message);
    }
}
