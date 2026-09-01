package com.ai.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @ClassName: SseController
 * @Author: JacksonLee
 * @Date: 2026/9/1
 * @Description: SSE协议流式编程
 */
@Slf4j
@RequestMapping("/sse")
@RestController
public class SseController {
    @RequestMapping("/data")
    public void data (HttpServletResponse response) throws IOException, InterruptedException {
        log.info("发起请求: data");
        response.setContentType("text/event-stream;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String s = "";
        for (int i = 0; i < 20; i++) {
            s = "data: " + new Date() + "\n\n";
            writer.write(s);
            writer.flush();
            Thread.sleep(1000L);
        }
    }

    @RequestMapping("/retry")
    public void retry (HttpServletResponse response) throws IOException, InterruptedException {
        log.info("发起请求: retry");
        response.setContentType("text/event-stream;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String s = "retry: 2000\n";
        s += "data: " + new Date() + "\n\n";
        writer.write(s);
        writer.flush();
    }

    @RequestMapping("/event")
    public void event (HttpServletResponse response) throws IOException, InterruptedException {
        log.info("发起请求: event");
        response.setContentType("text/event-stream;charset=utf-8");
        PrintWriter writer = response.getWriter();
        for (int i = 0; i < 10; i++) {
            String s = "event: foo\n";
            s += "data: " + new Date() + "\n\n";
            writer.write(s);
            writer.flush();
            Thread.sleep(1000L);
        }
    }

    @RequestMapping("/end")
    public void end (HttpServletResponse response) throws IOException, InterruptedException {
        log.info("发起请求: end");
        response.setContentType("text/event-stream;charset=utf-8");
        PrintWriter writer = response.getWriter();
        for (int i = 0; i < 10; i++) {
            String s = "event: foo\n";
            s += "data: " + new Date() + "\n\n";
            writer.write(s);
            writer.flush();
            Thread.sleep(1000L);
        }
        // 定义end事件，表示当前流传输结束
        writer.write("event: end\ndata: EOF\n\n");
        writer.flush();
    }
}
