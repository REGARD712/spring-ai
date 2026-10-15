package com.me.alibaba.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;

/**
 * 获取用户时区的当前日期和时间的工具类
 *
 * @ClassName: DateTimeTool
 * @Author: JacksonLee
 * @Date: 2026/10/14
 */
public class DateTimeTools {

    @Tool(description = "Get the current date and time in the user's timezone")
    String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
}
