package com.me.alibaba;

import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesis;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesisParam;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.alibaba.dashscope.utils.Constants;

public class Text2Video {
    static {
        // 以下为北京地域url，各地域的url不同，获取url：https://help.aliyun.com/zh/model-studio/text-to-video-api-reference
        Constants.baseHttpApiUrl = "https://llm-ql487nsxacardrij.cn-beijing.maas.aliyuncs.com/api/v1";
    }

    // 若没有配置环境变量，请用百炼API Key将下行替换为：apiKey="sk-xxx"
    // 各地域的API Key不同。获取API Key：https://help.aliyun.com/zh/model-studio/get-api-key
    static String apiKey = System.getenv("QWEN_API_KEY");

    public static void text2video() throws ApiException, NoApiKeyException, InputRequiredException {
        VideoSynthesis vs = new VideoSynthesis();
        VideoSynthesisParam param =
                VideoSynthesisParam.builder()
                        .apiKey(apiKey)
                        .model("wan2.2-t2v-plus")
                        .prompt("边缘光，低对比度，中近景，日光，左侧重构图，干净的单人镜头，暖色调，柔光，晴天光，侧光，白天，一个年轻的女孩坐在高草丛生的田野中，两条毛发蓬松的小毛驴站在她身后。女孩大约十一二岁，穿着简单的碎花裙子，头发扎成两条麻花辫，脸上带着纯真的笑容。她双腿交叉坐下，双手轻轻抚弄身旁的野花。小毛驴体型健壮，耳朵竖起，好奇地望着镜头方向。阳光洒在田野上，营造出温暖自然的画面感。")
                        .size("832*480")
                        .watermark(true)
                        .seed(12345)
                        .build();
        // 异步调用
        VideoSynthesisResult task = vs.asyncCall(param);
        System.out.println(JsonUtils.toJson(task));
        System.out.println("please wait...");

        //获取结果
        VideoSynthesisResult result = vs.wait(task, apiKey);
        System.out.println(JsonUtils.toJson(result));
    }

    public static void text2video2() throws ApiException, NoApiKeyException, InputRequiredException {
        VideoSynthesis vs = new VideoSynthesis();
        VideoSynthesisParam param =
                VideoSynthesisParam.builder()
                        .apiKey(apiKey)
                        .model("wan3.0-video")
                        .prompt("一幅史诗级可爱的场景。一只小巧可爱的卡通小猫将军，身穿细节精致的金色盔甲，头戴一个稍大的头盔，勇敢地站在悬崖上。他骑着一匹虽小但英勇的战马，说：“青海长云暗雪山，孤城遥望玉门关。黄沙百战穿金甲，不破楼兰终不还。”。悬崖下方，一支由老鼠组成的、数量庞大、无穷无尽的军队正带着临时制作的武器向前冲锋。这是一个戏剧性的、大规模的战斗场景，灵感来自中国古代的战争史诗。远处的雪山上空，天空乌云密布。整体氛围是“可爱”与“霸气”的搞笑和史诗般的融合。")
                        .audioUrl("https://help-static-aliyun-doc.aliyuncs.com/file-manage-files/zh-CN/20250923/hbiayh/%E4%BB%8E%E5%86%9B%E8%A1%8C.mp3")
                        .negativePrompt("")
                        .resolution("720P")
                        .ratio("16:9")
                        .duration(10)
                        .watermark(true)
                        .seed(12345)
                        .build();
        // 异步调用
        VideoSynthesisResult task = vs.asyncCall(param);
        System.out.println(JsonUtils.toJson(task));
        System.out.println("please wait...");

        //获取结果
        VideoSynthesisResult result = vs.wait(task, apiKey);
        System.out.println(JsonUtils.toJson(result));
    }

    public static void text2video3() throws ApiException, NoApiKeyException, InputRequiredException {
        VideoSynthesis vs = new VideoSynthesis();
        VideoSynthesisParam param =
                VideoSynthesisParam.builder()
                        .apiKey(apiKey)
                        .model("wan3.0-video")
                        .prompt("展现未来科技与自然和谐共存的美好愿景。 第1个镜头[0-2秒] 未来城市的空中花园全景，悬浮植物在微风中摇曳。 第2个镜头[2-4秒] 机器人园丁正在精心修剪植物，动作精准而优雅。 第3个镜头[4-7秒] 阳光透过透明穹顶洒下，照亮整个花园，展现科技与自然的完美融合。 第4个镜头[7-10秒] 镜头拉远，展现整个未来城市的壮观景象，空中花园只是其中的一部分。")
                        .resolution("720P")
                        .ratio("16:9")
                        .duration(10)
                        .watermark(true)
                        .seed(12345)
                        .build();
        // 异步调用
        VideoSynthesisResult task = vs.asyncCall(param);
        System.out.println(JsonUtils.toJson(task));
        System.out.println("please wait...");

        //获取结果
        VideoSynthesisResult result = vs.wait(task, apiKey);
        System.out.println(JsonUtils.toJson(result));
    }

    public static void main(String[] args) {
        try {
            text2video();
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }
}
