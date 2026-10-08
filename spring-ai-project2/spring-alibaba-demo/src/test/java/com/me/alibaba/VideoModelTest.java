package com.me.alibaba;

import com.alibaba.cloud.ai.dashscope.api.DashScopeVideoApi;
import com.alibaba.cloud.ai.dashscope.video.DashScopeVideoOptions;
import com.alibaba.cloud.ai.dashscope.video.VideoPrompt;
import com.alibaba.cloud.ai.dashscope.video.VideoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.support.RetryTemplate;
import com.alibaba.cloud.ai.dashscope.video.DashScopeVideoModel;

@SpringBootTest
public class VideoModelTest {
    private static final String API_KEY_ENV = "QWEN_API_KEY";

    private final DashScopeVideoModel videoModel;

    public VideoModelTest() {
        DashScopeVideoApi videoApi = DashScopeVideoApi.builder().apiKey(System.getenv(API_KEY_ENV)).build();
        RetryTemplate retryTemplate = RetryTemplate.builder().maxAttempts(30) // Increase max attempts for video generation
                .fixedBackoff(10000) // 10 seconds between retries
                .build();

        this.videoModel = DashScopeVideoModel.builder()
                .videoApi(videoApi)
                .retryTemplate(retryTemplate)
                .build();

    }

    @Test
    void text2Video () {
        String prompt = "一幅史诗级可爱的场景。一只小巧可爱的卡通小猫将军，身穿细节精致的金色盔甲，头戴一个稍大的头盔，勇敢地站在悬崖上。他骑着一匹虽小但英勇的战马，说：\\\"青海长云暗雪山，孤城遥望玉门关。黄沙百战穿金甲，不破楼兰终不还。\\\"悬崖下方，一支由老鼠组成的、数量庞大、无穷无尽的军队正带着临时制作的武器向前冲锋。这是一个戏剧性的、大规模的战斗场景，灵感来自中国古代的战争史诗。远处的雪山上空，天空乌云密布。整体氛围是\\\"可爱\\\"与\\\"霸气\\\"的搞笑和史诗般的融合。";
        VideoResponse videoResponse = videoModel.call(new VideoPrompt(prompt));
        String responseVideoUrl = videoResponse.getResult().getOutput().getVideoUrl();
        System.out.println(responseVideoUrl);
    }

    @Test
    void text2Video2 () {
        String prompt = "一只小猫在月光下奔跑";
        DashScopeVideoOptions options = DashScopeVideoOptions.builder()
                .model("wan2.2-t2v-plus")
                .size("1920*1080")
                .build();
        VideoResponse videoResponse = videoModel.call(new VideoPrompt(prompt, options));
        String responseVideoUrl = videoResponse.getResult().getOutput().getVideoUrl();
        System.out.println(responseVideoUrl);
    }
}
