package com.ai.controller;

import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: DeepSeekController
 * @Author: JacksonLee
 * @Date: 2026/8/28
 * @Description: SpringAi接入DeepSeekAPI
 */
@RequestMapping("/ds")
@RestController
public class DeepSeekController {
    @Autowired
    private DeepSeekChatModel deepSeekChatModel;

    @RequestMapping("/chat")
    public String chat (String message) {
        return deepSeekChatModel.call(message);
    }
}
