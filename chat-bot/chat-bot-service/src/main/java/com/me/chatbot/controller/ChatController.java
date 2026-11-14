package com.me.chatbot.controller;

import com.me.chatbot.entity.ChatInfo;
import com.me.chatbot.entity.MessageVO;
import com.me.chatbot.repository.ChatHistoryRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: ChatController
 * @Author: JacksonLee
 * @Date: 2026/9/9
 * @Description: 简单对话相关接口
 */
@Slf4j
@RequestMapping("/chat")
@RestController
public class ChatController {
    private final ChatClient chatClient;

    @Resource
    private ChatHistoryRepository memoryChatHistoryRepository;

    @Resource
    private ChatMemory chatMemory;

//    public ChatController(ChatClient ollamaChatClient) {
//        this.chatClient = ollamaChatClient;
//    }

    // 改为qwen
    public ChatController(ChatClient qwenChatClient) {
        this.chatClient = qwenChatClient;
    }

    /**
     * @param prompt 用户输入的消息内容
     * @param chatId 会话的id, 用于实现会话的记忆
     * @return Flux<String> 流式返回的机器人回复内容
     * @Description: 与机器人对话（流式返回）接口
     */
    @RequestMapping(value = "/stream", produces = "text/html;charset=utf-8")
    public Flux<String> stream (@RequestParam("prompt") String prompt,
                                @RequestParam("chatId") String chatId) {
        log.info("chatId: {}, prompt: {}", chatId, prompt);
        // 存储会话
        memoryChatHistoryRepository.save(chatId, prompt);
        // 返回智能体的响应
        return this.chatClient.prompt()
                .user(prompt)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }

    /**
     * @param
     * @return List<ChatInfo> 返回内存中的历史会话
     * @description: 获取历史会话列表接口
     */
    @RequestMapping("/getChatIds")
    public List<ChatInfo> getChatIds () {
        return memoryChatHistoryRepository.getChats();
    }

    /**
     * @param chatId 会话编号
     * @return List<MessageVO> 会话记录
     * @description: 获取会话记录接口
     */
    @RequestMapping("/getChatHistory")
    public List<MessageVO> getChatHistory (@RequestParam("chatId") String chatId) {
        log.info("获取会话记录, chatId: {}", chatId);
        List<Message> messages = chatMemory.get(chatId); // 获取会话记录
        return messages.stream().map(MessageVO::new).collect(Collectors.toList());
    }

    /**
     * @param chatId 会话编号
     * @return Boolean true:成功; false:失败
     * @description: 删除会话
     */
    @RequestMapping("/deleteChat")
    public Boolean deleteChat (@RequestParam("chatId") String chatId) {
        log.info("删除会话, chatId: {}", chatId);
        try {
            memoryChatHistoryRepository.clearByCharId(chatId);
            chatMemory.clear(chatId);
        } catch (Exception e) {
            log.error("删除会话失败, chatId: {}", chatId);
            return false;
        }
        return true;
    }
}
