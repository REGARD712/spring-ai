package com.me.alibaba.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.me.alibaba.tool.DateTimeTools;
import com.me.alibaba.tool.WeatherTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

/**
 * Tool-Calling examples1
 *
 * @ClassName: ChatClientController
 * @Author: JacksonLee
 * @Date: 2026/10/14
 */
@RequestMapping("/chat")
@RestController
public class ChatClientController {
    private final ChatClient chatClient;

    public ChatClientController(DashScopeChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultTools(new DateTimeTools())
                .build();
    }

    @Autowired
    public ChatClientController(DashScopeChatModel chatModel, ToolCallback weatherTools) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultTools(new DateTimeTools())  // 时间工具
                .defaultToolCallbacks(weatherTools) // 天气工具
                .build();
    }

    /**
     * 通过注解声明方式调用工具
     * @param message 用户提示词
     * @return String
     */
    @RequestMapping("/call")
    public String call (String message) {
        return chatClient
                .prompt()
                .user(message)
//                .tools(new DateTimeTool())
                .call()
                .content();
    }

    /**
     * 通过编程方式调用工具
     * @param message 用户提示词
     * @return String
     */
    @RequestMapping("/callByProgramming")
    public String callByProgramming (String message) {
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

        return chatClient
                .prompt()
                .user(message)
                .toolCallbacks(toolCallback)
                .call()
                .content();
    }
}
