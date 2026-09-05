package com.me.alibaba.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @ClassName: MultiModelController
 * @Author: JacksonLee
 * @Date: 2026/9/5
 * @Description: 多模态
 */
@RequestMapping("/multi")
@RestController
public class MultiModelController {
    private final ChatClient chatClient;

    public MultiModelController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @RequestMapping("/image")
    public String image (String prompt) throws Exception {
        String imageUrl = "https://dashscope.oss-cn-beijing.aliyuncs.com/images/dog_and_girl.jpeg";
        List<Media> mediaList = List.of(new Media(MimeTypeUtils.IMAGE_JPEG, new URI(imageUrl).toURL().toURI()));

        // 定义用户提示词
        UserMessage userMessage = UserMessage.builder()
                .text(prompt)
                .media(mediaList)
                .build();

        // 调用AI模型进行多模态处理
        return chatClient.prompt(new Prompt(userMessage)).call().content();
    }
}
