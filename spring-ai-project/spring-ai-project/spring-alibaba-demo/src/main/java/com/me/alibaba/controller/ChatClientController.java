package com.me.alibaba.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @ClassName: ChatClientController
 * @Author: JacksonLee
 * @Date: 2026/9/5
 * @Description: ChatClient调用阿里通义模型API示例
 */
@RequestMapping("/chat")
@RestController
public class ChatClientController {
//    private final ChatClient chatClient;
//
//    public ChatClientController(ChatClient.Builder builder) {
//        this.chatClient = builder.build();
//    }

    @Autowired
    private ChatClient chatClient;

    @RequestMapping("/call")
    public String call (String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @RequestMapping(value = "/callStream", produces = "text/html;charset=utf-8")
    public Flux<String> callStream (String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

    record ActorFilms(String actor, List<String> movies) {}
    @RequestMapping("/entity")
    public String entity (String actor) {
        ActorFilms actorFilms = chatClient.prompt()
                .user(String.format("请告诉我%s的电影作品有哪些？", actor))
                .call()
                .entity(ActorFilms.class);
        assert actorFilms != null;
        return actorFilms.toString();
    }

    @RequestMapping("/word")
    String word (String message, String word) {
        return this.chatClient.prompt()
                        .system(sp -> sp.param("word", word))
                        .user(message)
                        .call()
                        .content();
    }
}
