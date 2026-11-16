package com.me.ticket.service;

import com.me.ticket.entity.TicketInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TicketServiceTest {

    @Autowired
    private TicketService ticketService;

    @Test
    void createTicket() {
        String ticket = ticketService.createTicket("测试工单", "测试工单描述", "123456");
        System.out.println(ticket);
    }

    @Test
    void queryTicket() {
        TicketInfo ticketInfo = ticketService.queryTicket("BIT-1794816876496-1dce0554-00bd-4725-ab48-c98c7feb9bf3");
        System.out.println(ticketInfo);
    }
}