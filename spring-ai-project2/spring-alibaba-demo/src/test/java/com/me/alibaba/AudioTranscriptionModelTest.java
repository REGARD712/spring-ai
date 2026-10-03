package com.me.alibaba;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioTranscriptionModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioTranscriptionOptions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

@SpringBootTest
public class AudioTranscriptionModelTest {
    @Autowired
    private DashScopeAudioTranscriptionModel transcriptionModel;

    @Test
    void stt () {
        Resource resource = new DefaultResourceLoader().getResource("https://dashscope.oss-cn-beijing.aliyuncs.com/samples/audio/paraformer/hello_world_female2.wav");

        AudioTranscriptionResponse response = transcriptionModel.call(
                new AudioTranscriptionPrompt(
                        resource,
                        DashScopeAudioTranscriptionOptions.builder()
                                .build()
                )
        );
        System.out.println(response.getResult().getOutput());
    }
}
