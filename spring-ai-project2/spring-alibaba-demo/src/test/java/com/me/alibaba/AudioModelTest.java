package com.me.alibaba;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import com.alibaba.dashscope.audio.tts.SpeechSynthesisAudioFormat;
import com.alibaba.dashscope.audio.tts.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.tts.SpeechSynthesizer;
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
    private DashScopeAudioSpeechModel dashScopeSpeechSynthesisModel;

    private final String text = "小池, 泉眼无声惜细流, 树阴照水爱晴柔. 小荷才露尖尖角, 早有蜻蜓立上头";

    @Test
    void tts() {
        SpeechSynthesisResponse response = dashScopeSpeechSynthesisModel.call(new SpeechSynthesisPrompt(text));
        File file = new File(System.getProperty("user.dir") + "/output.mp3");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            ByteBuffer audio = response.getResult().getOutput().getAudio();
            fos.write(audio.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void tts2() {
        DashScopeAudioSpeechOptions options = DashScopeAudioSpeechOptions.builder()
                .model("cosyvoice-v3-flash")
                .voice("longhuhu_v3")
                .speed(0.5f)
                .build();
        SpeechSynthesisResponse response = dashScopeSpeechSynthesisModel.call(new SpeechSynthesisPrompt(text, options));
        File file = new File(System.getProperty("user.dir") + "/output.mp3");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            ByteBuffer audio = response.getResult().getOutput().getAudio();
            fos.write(audio.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void tts3() {
        SpeechSynthesizer synthesizer = new SpeechSynthesizer();
        SpeechSynthesisParam param = SpeechSynthesisParam.builder()
                // 若没有将API Key配置到环境变量中，需将下面这行代码注释放开，并将apiKey替换为自己的API Key
//                .apiKey("your-api-key")
                .model("sambert-zhichu-v1")
                .text(text)
                .format(SpeechSynthesisAudioFormat.WAV)
                .build();

        File file = new File(System.getProperty("user.dir")+"/output.wav");
        // 提交同步合成任务，获取完整的音频数据
        ByteBuffer audio = synthesizer.call(param);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(audio.array());
            System.out.println("synthesis done!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
