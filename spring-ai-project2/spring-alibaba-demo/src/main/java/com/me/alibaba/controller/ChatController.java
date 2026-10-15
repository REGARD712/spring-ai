package com.me.alibaba.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.me.alibaba.tool.DateTimeTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Tool-Calling examples
 *
 * @ClassName: ChatController
 * @Author: JacksonLee
 * @Date: 2026/10/14
 */
@RequestMapping("/chat")
@RestController
public class ChatController {
//    private final ChatClient chatClient;
//
//    public ChatController(DashScopeChatModel chatModel) {
//        this.chatClient = ChatClient.builder(chatModel)
//                .defaultTools(new DateTimeTool())
//                .build();
//    }
//
//    @RequestMapping("/call")
//    public String call (String message) {
//        return chatClient
//                .prompt()
//                .user(message)
////                .tools(new DateTimeTool())
//                .call()
//                .content();
//    }

    private ChatModel chatModel;

    public ChatController(DashScopeChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 向ChatModel添加工具
     * @param message 用户提示词
     * @return void
     */
    @RequestMapping("/call")
    public String call (String message) {
        return chatModel.call(message);
    }

    @RequestMapping("/callByTools")
    public String callByTools (String message) {
        ToolCallback[] dateTimeTools = ToolCallbacks.from(new DateTimeTool());
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(dateTimeTools)
                .build();
        Prompt prompt = new Prompt(message, chatOptions);
        return chatModel.call(prompt).getResult().getOutput().getText();
    }
}
