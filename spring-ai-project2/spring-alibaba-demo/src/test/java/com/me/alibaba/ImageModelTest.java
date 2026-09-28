package com.me.alibaba;

import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImageModelTest {
    @Autowired
    private DashScopeImageModel imageModel;

    @Test
    void test () {
        ImageResponse imageResponse = imageModel.call(new ImagePrompt("一个小孩子在海边玩耍"));
        String imgUrl = imageResponse.getResult().getOutput().getUrl();
        System.out.println(imgUrl);
    }
}
