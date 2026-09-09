package com.me.chatbot.entity;

import lombok.Data;
import org.springframework.ai.chat.messages.Message;

/**
 * @ClassName: MessageV0
 * @Author: JacksonLee
 * @Date: 2026/9/9
 * @Description: Message转换处理
 */
@Data
public class MessageVO {
    String role;
    String content;

    public MessageVO (Message message) {
        switch (message.getMessageType()) {
            case USER -> {this.role = "user"; break;}
            case ASSISTANT -> {this.role = "assistant"; break;}
            case SYSTEM -> {this.role = "system"; break;}
            case TOOL -> {this.role = "tool"; break;}
        }
        this.content = message.getText();
    }
}
