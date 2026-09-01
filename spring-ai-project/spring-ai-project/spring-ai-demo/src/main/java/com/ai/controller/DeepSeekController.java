package com.ai.controller;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;

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

    @RequestMapping("/callByPrompt")
    public String callByPrompt (String message) {
        ChatResponse response = deepSeekChatModel.call(new Prompt(message));
        return Objects.requireNonNull(response.getResult()).getOutput().getText();
    }

    @RequestMapping("/role")
    public String role (String message) {
        SystemMessage systemMessage = new SystemMessage("你是一个资深的Java开发工程师，精通SpringBoot、SpringCloud、SpringAi等技术栈。");
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(systemMessage, userMessage);
        ChatResponse response = deepSeekChatModel.call(prompt);
        return Objects.requireNonNull(response.getResult()).getOutput().getText();
    }

    @RequestMapping(value = "/callByStream", produces = "text/html;charset=utf-8")
    public Flux<String> callByStream (String message) {
        Flux<ChatResponse> response = deepSeekChatModel.stream(new Prompt(message));
        return response.filter(r -> r.getResult() != null
                && r.getResult().getOutput() != null
                && r.getResult().getOutput().getText() != null)
                .map(r -> r.getResult().getOutput().getText());
    }
}
