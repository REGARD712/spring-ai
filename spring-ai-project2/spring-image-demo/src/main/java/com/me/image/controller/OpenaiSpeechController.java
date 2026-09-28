package com.me.image.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * OpenAi的语音模型
 *
 * @ClassName: OpenaiSpeechController
 * @Author: JacksonLee
 * @Date: 2026/9/28
 */
@Slf4j
@RequestMapping("/speech")
@RestController
public class OpenaiSpeechController {
    @Autowired
    private OpenAiAudioSpeechModel openAiAudioSpeechModel;

    @RequestMapping("/tts")
    public void tts () {
        OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
                .model("gpt-4o-mini-tts")
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0f)
                .build();

        SpeechPrompt speechPrompt = new SpeechPrompt("Hello, this is a text-to-speech example.", speechOptions);
        SpeechResponse response = openAiAudioSpeechModel.call(speechPrompt);

        // 将响应内容保存在文件中
        File file = new File(System.getProperty("user.dir") + "/output.mp3");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(response.getResult().getOutput());
        } catch (IOException e) {
            log.warn("e: ", e);
        }
    }
}
