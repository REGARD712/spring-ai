package com.me.ollama.controller;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: OllamaController
 * @Author: JacksonLee
 * @Date: 2026/9/3
 * @Description: 测试：通过Ollama将DeepSeek接入SpringAI
 */
@RequestMapping("/ollama")
@RestController
public class OllamaController {
    @Autowired
    private OllamaChatModel ollamaChatModel;

    @RequestMapping("/chat")
    public String chat (String message) {
        return ollamaChatModel.call(message);
    }
}
