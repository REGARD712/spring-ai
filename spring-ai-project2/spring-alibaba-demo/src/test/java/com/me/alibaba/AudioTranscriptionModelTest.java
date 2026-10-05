package com.me.alibaba;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioTranscriptionModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioTranscriptionOptions;
import com.alibaba.dashscope.audio.asr.recognition.Recognition;
import com.alibaba.dashscope.audio.asr.recognition.RecognitionParam;
import com.alibaba.dashscope.audio.asr.recognition.RecognitionResult;
import com.alibaba.dashscope.audio.asr.transcription.Transcription;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionParam;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionQueryParam;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionResult;
import com.alibaba.dashscope.common.ResultCallback;
import com.google.gson.GsonBuilder;
import io.micrometer.core.instrument.util.TimeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class AudioTranscriptionModelTest {
    @Autowired
    private DashScopeAudioTranscriptionModel transcriptionModel;

    @Test
    void stt() {
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

    @Test
    void sttByDashscopeSdk() {
        // 创建转写请求参数
        TranscriptionParam param =
                TranscriptionParam.builder()
                        // 若没有将API Key配置到环境变量中，需将apiKey替换为自己的API Key
//                        .apiKey("your-api-key")
                        .model("paraformer-v2")
                        // “language_hints”只支持paraformer-v2模型
                        .parameter("language_hints", new String[]{"zh", "en"})
                        .fileUrls(
                                Arrays.asList(
                                        "https://dashscope.oss-cn-beijing.aliyuncs.com/samples/audio/paraformer/hello_world_male2.wav",
                                        "https://dashscope.oss-cn-beijing.aliyuncs.com/samples/audio/paraformer/hello_world_female2.wav"))
                        .build();
        try {
            Transcription transcription = new Transcription();
            // 提交转写请求
            TranscriptionResult result = transcription.asyncCall(param);
            System.out.println("RequestId: " + result.getRequestId());
            // 阻塞等待任务完成并获取结果
            result = transcription.wait(
                    TranscriptionQueryParam.FromTranscriptionParam(param, result.getTaskId()));
            // 打印结果
            System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(result.getOutput()));
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
        System.exit(0);
    }
    
    /**
     * https://help.aliyun.com/zh/model-studio/paraformer-real-time-speech-recognition-java-sdk?spm=a2c4g.11186623.help-menu-2400256.d_2_8_2_3_3_4_2.30e25860yVRO9U&scm=20140722.H_2869146._.OR_help-T_cn~zh-V_1#%E9%9D%9E%E6%B5%81%E5%BC%8F%E8%B0%83%E7%94%A8
     * @param  
     * @return void
     */
    @Test
    void sttRecognitionNonStreaming() {
        // 非流式调用：针对本地文件进行识别，并一次性返回完整的处理结果。适合处理录制好的音频。
        // 创建Recognition实例
        Recognition recognizer = new Recognition();
        // 创建RecognitionParam
        RecognitionParam param =
                RecognitionParam.builder()
                        // 若没有将API Key配置到环境变量中，需将下面这行代码注释放开，并将apiKey替换为自己的API Key
                        //.apiKey("yourApikey")
                        .model("paraformer-realtime-v2")
                        .format("wav")
                        .sampleRate(16000)
                        // “language_hints”只支持paraformer-realtime-v2模型
                        .parameter("language_hints", new String[]{"zh", "en"})
                        .build();

        try {
            String filePath = System.getProperty("user.dir") + "/hello_world_male_16k_16bit_mono.wav";
            System.out.println("识别结果：" + recognizer.call(param, new File(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 任务结束后关闭 WebSocket 连接
            recognizer.getDuplexApi().close(1000, "bye");
        }
        System.out.println(
                "[Metric] requestId: "
                        + recognizer.getLastRequestId()
                        + ", first package delay ms: "
                        + recognizer.getFirstPackageDelay()
                        + ", last package delay ms: "
                        + recognizer.getLastPackageDelay());
        System.exit(0);
    }

    @Test
    void sttRecognitionStreaming () throws InterruptedException {
        // 双向流式调用：可直接对音频流进行识别，并实时输出结果。音频流可以来自外部设备（如麦克风）或从本地文件读取。适合需要即时反馈的场景。
        // 基于回调--提交单个语音实时转写任务，通过实现回调接口的方式流式输出实时识别结果。识别本地录音文件
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        String filePath = System.getProperty("user.dir") + "/hello_world_male_16k_16bit_mono.wav";
        executorService.submit(new RealtimeRecognitionTask(Paths.get(filePath)));
        executorService.shutdown();

        // wait for all tasks to complete
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        System.exit(0);
    }
    class RealtimeRecognitionTask implements Runnable {
        private Path filepath;

        public RealtimeRecognitionTask(Path filepath) {
            this.filepath = filepath;
        }

        @Override
        public void run() {
            RecognitionParam param = RecognitionParam.builder()
                    // 若没有将API Key配置到环境变量中，需将apiKey替换为自己的API Key
                    //.apiKey("yourApikey")
                    .model("paraformer-realtime-v2")
                    .format("wav")
                    .sampleRate(16000)
                    // “language_hints”只支持paraformer-realtime-v2模型
                    .parameter("language_hints", new String[]{"zh", "en"})
                    .build();
            Recognition recognizer = new Recognition();

            String threadName = Thread.currentThread().getName();

            ResultCallback<RecognitionResult> callback = new ResultCallback<RecognitionResult>() {
                @Override
                public void onEvent(RecognitionResult message) {
                    if (message.isSentenceEnd()) {

                        System.out.println(TimeUtils.getTimestamp()+" "+
                                "[process " + threadName + "] Final Result:" + message.getSentence().getText());
                    } else {
                        System.out.println(TimeUtils.getTimestamp()+" "+
                                "[process " + threadName + "] Intermediate Result: " + message.getSentence().getText());
                    }
                }

                @Override
                public void onComplete() {
                    System.out.println(TimeUtils.getTimestamp()+" "+"[" + threadName + "] Recognition complete");
                }

                @Override
                public void onError(Exception e) {
                    System.out.println(TimeUtils.getTimestamp()+" "+
                            "[" + threadName + "] RecognitionCallback error: " + e.getMessage());
                }
            };

            try {
                recognizer.call(param, callback);
                // Please replace the path with your audio file path
                System.out.println(TimeUtils.getTimestamp()+" "+"[" + threadName + "] Input file_path is: " + this.filepath);
                // Read file and send audio by chunks
                FileInputStream fis = new FileInputStream(this.filepath.toFile());
                // chunk size set to 1 seconds for 16KHz sample rate
                byte[] buffer = new byte[3200];
                int bytesRead;
                // Loop to read chunks of the file
                while ((bytesRead = fis.read(buffer)) != -1) {
                    ByteBuffer byteBuffer;
                    // Handle the last chunk which might be smaller than the buffer size
                    System.out.println(TimeUtils.getTimestamp()+" "+"[" + threadName + "] bytesRead: " + bytesRead);
                    if (bytesRead < buffer.length) {
                        byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead);
                    } else {
                        byteBuffer = ByteBuffer.wrap(buffer);
                    }

                    recognizer.sendAudioFrame(byteBuffer);
                    buffer = new byte[3200];
                    Thread.sleep(100);
                }
                System.out.println(TimeUtils.getTimestamp()+" "+ LocalDateTime.now());
                recognizer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 任务结束后关闭 Websocket 连接
                recognizer.getDuplexApi().close(1000, "bye");
            }

            System.out.println(
                    "["
                            + threadName
                            + "][Metric] requestId: "
                            + recognizer.getLastRequestId()
                            + ", first package delay ms: "
                            + recognizer.getFirstPackageDelay()
                            + ", last package delay ms: "
                            + recognizer.getLastPackageDelay());
        }
    }
    class TimeUtils {
        private static final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        public static String getTimestamp() {
            return LocalDateTime.now().format(formatter);
        }
    }
}
