package com.me.chatbot.repository;

import com.me.chatbot.entity.ChatInfo;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: MemoryChatHistoryRepository
 * @Author: JacksonLee
 * @Date: 2026/9/9
 * @Description: 历史会话接口的实现类
 */
@Repository
public class MemoryChatHistoryRepository implements ChatHistoryRepository {
    private final Map<String, String> chatInfos = new LinkedHashMap<>();

    @Override
    public void save(String chatId, String title) {
        chatInfos.put(chatId, title);
    }

    @Override
    public void clearByCharId(String chatId) {
        chatInfos.remove(chatId);
    }

    @Override
    public List<ChatInfo> getChats() {
        return chatInfos.entrySet()
                .stream()
                .map(charInfo -> new ChatInfo(charInfo.getKey(), charInfo.getValue()))
                .collect(Collectors.toList());
    }
}
