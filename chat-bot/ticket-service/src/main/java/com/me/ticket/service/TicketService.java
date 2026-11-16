package com.me.ticket.service;

import com.me.ticket.entity.TicketInfo;
import com.me.ticket.mapper.TicketMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Slf4j
@Service
public class TicketService {

    private final TicketMapper ticketMapper;

    public TicketService(TicketMapper ticketMapper) {
        this.ticketMapper = ticketMapper;
    }

    @Tool(description = "根据标题、描述和关联的会话ID创建工单")
    public String createTicket(
            @ToolParam(description = "工单标题,必填") String title,
            @ToolParam(description = "工单描述,必填") String description,
            @ToolParam(description = "关联的会话ID,必填") String relatedChatId
    ) {
        // 非空校验
        if (!StringUtils.hasLength(title)
        || !StringUtils.hasLength(description)
        || !StringUtils.hasLength(relatedChatId)) {
            return "工单创建失败,参数不能为空: " +
                    "title = " + title + ", " +
                    "description = " + description + ", " +
                    "relatedChatId = " + relatedChatId;
        }

        // 创建工单
        TicketInfo ticketInfo = parseTicketInfo(title, description, relatedChatId);

        try {
            Integer rows = ticketMapper.insertTicket(ticketInfo);
        } catch (Exception e) {
            log.error("工单创建失败,工单ID: {}", ticketInfo.getTicketId(), e);
            return "工单创建失败,工单ID: " + ticketInfo.getTicketId();
        }

        return "工单创建成功,工单ID: " + ticketInfo.getTicketId();
    }

    @Tool(description = "根据工单ID查询工单详情")
    public TicketInfo queryTicket(@ToolParam(description = "工单ID,必填") String ticketId) {
        log.info("查询工单,工单ID: {}", ticketId);
        return ticketMapper.selectTicketById(ticketId);
    }

    private TicketInfo parseTicketInfo(String title, String description, String relatedChatId) {
        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setTicketId(generateTicketId());
        ticketInfo.setTitle(title);
        ticketInfo.setDescription(description);
        ticketInfo.setRelatedChatId(relatedChatId);
        return ticketInfo;
    }

    /**
     * 生成工单ID: BIT-时间戳-UUID
     *
     * @param
     * @return String
     */
    private String generateTicketId() {
        return "BIT-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
    }

}
