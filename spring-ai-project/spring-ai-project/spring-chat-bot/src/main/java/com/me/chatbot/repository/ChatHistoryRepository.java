package com.me.chatbot.repository;

import com.me.chatbot.entity.ChatInfo;

import java.util.List;

/**
 * @InterfaceName：ChatHistoryRepository
 * @Author: JacksonLee
 * @Date: 2026/9/9
 * @Description: 历史会话相关接口
 */
public interface ChatHistoryRepository {
    /**
     * @param chatId 会话的编号
     * @param title 本次会话的标题
     * @return void
     * @description: 保存会话记录
     */
    void save (String chatId, String title);

    /**
     * @param chatId 会话编号
     * @return void
     * @description: 根据会话编号删除对应的会话
     */
    void clearByCharId (String chatId);

    /**
     * @param
     * @return List<ChatInfo> 会话列表
     * @description: 获取历史会话列表
     */
    List<ChatInfo> getChats ();
}
