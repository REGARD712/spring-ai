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

    private final String text = "白日依山尽，黄河入海流，欲穷千里目，更上一层楼。";

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
