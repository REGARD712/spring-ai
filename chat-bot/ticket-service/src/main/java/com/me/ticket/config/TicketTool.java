package com.me.ticket.config;

import com.me.ticket.service.TicketService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketTool {

    /**
     * 配置工具回调提供程序
     *
     * @param ticketService 工单服务工具
     * @return ToolCallbackProvider
     */
    @Bean
    public ToolCallbackProvider toolCallbackProvider(TicketService ticketService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(ticketService)
                .build();
    }

}
