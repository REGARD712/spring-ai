package com.me.ticket.mapper;

import com.me.ticket.entity.TicketInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TicketMapper {

    @Insert("""
        INSERT INTO ticket_info
        (ticket_id, title, description, related_chat_id)
        VALUES
        (#{ticketId}, #{title}, #{description}, #{relatedChatId})
    """)
    Integer insertTicket(TicketInfo ticketInfo);

    @Select("""
        SELECT * FROM ticket_info WHERE ticket_id = #{ticketId}
    """)
    TicketInfo selectTicketById(String ticketId);

}
