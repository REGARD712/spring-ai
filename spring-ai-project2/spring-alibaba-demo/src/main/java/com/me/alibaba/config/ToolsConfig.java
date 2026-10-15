package com.me.alibaba.config;

import com.me.alibaba.tool.WeatherTools;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Tool配置类
 *
 * @ClassName: ToolsConfig
 * @Author: JacksonLee
 * @Date: 2026/10/15
 */
@Configuration
public class ToolsConfig {

    @Bean
    public ToolCallback weatherTools () {
        Method method = ReflectionUtils.findMethod(WeatherTools.class,
                "getCurrentWeatherByCityName", String.class);

        assert method != null;
        return MethodToolCallback.builder()
                .toolDefinition(ToolDefinitions
                        .builder(method)
                        .description("根据城市名获取城市天气")
                        .build())
                .toolMethod(method)
                .toolObject(new WeatherTools())
                .build();
    }
}
