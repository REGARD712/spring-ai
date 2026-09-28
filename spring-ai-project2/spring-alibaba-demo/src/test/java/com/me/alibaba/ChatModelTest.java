package com.me.alibaba;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatModelTest {
    @Autowired
    private DashScopeChatModel chatModel;

    @Test
    void contextLoads() {
        String response = chatModel.call("你现在使用什么模型？");
        System.out.println(response);
    }
}
