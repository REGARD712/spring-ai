package com.me.chatbot.entity;

import lombok.Data;

/**
 * @ClassName: ChatInfo
 * @Author: JacksonLee
 * @Date: 2026/9/9
 * @Description: 对话历史信息
 */
@Data
public class ChatInfo {
    public ChatInfo(String chatId, String title) {
        this.chatId = chatId;
        // 对标题截断处理
        this.title = title==null ? "无标题" : (title.length()>=15 ? title.substring(0,15) : title);
    }

    private String chatId;
    private String title;
}
