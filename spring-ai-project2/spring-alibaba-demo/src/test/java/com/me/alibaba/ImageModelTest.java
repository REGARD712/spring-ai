package com.me.alibaba;

import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
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

    @Test
    void test2 () {
        DashScopeImageOptions options = DashScopeImageOptions.builder()
                .withStyle("<oil painting>")
                .build();
        ImageResponse imageResponse = imageModel.call(new ImagePrompt("一个小孩子在海边玩耍", options));
        String imgUrl = imageResponse.getResult().getOutput().getUrl();
        System.out.println(imgUrl);
    }

    @Test
    void test3 () {
        ImageResponse imageResponse = imageModel.call(new ImagePrompt("一副典雅庄重的对联悬挂于厅堂之中, 房间是个安静古典的中式布置, 桌子上放着一些青花瓷, 对联上\n" +
                "左书\"义本生知人机同道善思新\", 右书\"通云赋智乾坤启数高志远\", 横批\"智启通义\", 字体飘逸,\n" +
                "中间挂在一着一副中国风的画作, 内容是岳阳楼"));
        String imgUrl = imageResponse.getResult().getOutput().getUrl();
        System.out.println(imgUrl);
    }
}
