package com.me.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringImageDemoApplication {
    public static void main(String[] args) {
        System.setProperty("http.proxyHost", System.getenv("proxyHost"));// 配置代理ip(环境变量)
        System.setProperty("https.proxyHost", System.getenv("proxyHost"));
        System.setProperty("http.proxyPort", "9876"); // 配置代理端口号
        System.setProperty("https.proxyPort", "9876");
        SpringApplication.run(SpringImageDemoApplication.class, args);
    }
}
