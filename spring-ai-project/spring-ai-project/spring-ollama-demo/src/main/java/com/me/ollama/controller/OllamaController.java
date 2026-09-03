package com.me.ollama.controller;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @ClassName: OllamaController
 * @Author: JacksonLee
 * @Date: 2026/9/3
 * @Description: 通过Ollama将DeepSeek接入SpringAI
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

    @RequestMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream (String message) {
        return ollamaChatModel.stream(message);
    }
}
