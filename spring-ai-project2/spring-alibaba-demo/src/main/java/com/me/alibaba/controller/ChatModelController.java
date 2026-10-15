package com.me.alibaba.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.me.alibaba.tool.DateTimeTools;
import com.me.alibaba.tool.WeatherTools;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

/**
 * Tool-Calling examples2
 *
 * @ClassName: ChatModelController
 * @Author: JacksonLee
 * @Date: 2026/10/15
 */
@RequestMapping("/chatModel")
@RestController
public class ChatModelController {
    private final ChatModel chatModel;

    public ChatModelController(DashScopeChatModel chatModel) {
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
        ToolCallback[] dateTimeTools = ToolCallbacks.from(new DateTimeTools());
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(dateTimeTools)
                .build();
        Prompt prompt = new Prompt(message, chatOptions);
        return chatModel.call(prompt).getResult().getOutput().getText();
    }

    /**
     * 通过编程方式调用工具
     * @param message 用户提示词
     * @return String
     */
    @RequestMapping("/callByProgrammingModel")
    public String callByProgrammingModel (String message) {
        Method method = ReflectionUtils.findMethod(WeatherTools.class,
                "getCurrentWeatherByCityName", String.class);

        assert method != null;
        ToolCallback toolCallback = MethodToolCallback.builder()
                .toolDefinition(ToolDefinitions
                        .builder(method)
                        .description("根据城市名获取城市天气")
                        .build())
                .toolMethod(method)
                .toolObject(new WeatherTools())
                .build();

        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(toolCallback)
                .build();
        Prompt prompt = new Prompt(message, chatOptions);

        return chatModel.call(prompt).getResult().getOutput().getText();
    }
}
