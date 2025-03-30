package com.example.demo.dto;

import com.example.demo.entity.Ticket.TicketType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequest {
    private String ticketType;

    public String getTicketType() {
        return ticketType;
    }
}

