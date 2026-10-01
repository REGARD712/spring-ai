package com.me.alibaba;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@SpringBootTest
public class AudioModelTest {
    @Autowired
    private DashScopeSpeechSynthesisModel dashScopeSpeechSynthesisModel;

    private final String text = "小池, 泉眼无声惜细流, 树阴照水爱晴柔. 小荷才露尖尖角, 早有蜻蜓立上头";

    @Test
    void tts () {
        SpeechSynthesisResponse response = dashScopeSpeechSynthesisModel.call(new SpeechSynthesisPrompt(text));
        File file = new File(System.getProperty("user.dir") + "/output.mp3");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            ByteBuffer audio = response.getResult().getOutput().getAudio();
            fos.write(audio.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
