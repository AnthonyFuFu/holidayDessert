package com.holidaydessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketOrderMessageDto {
    private Integer typeId;
    private Integer memId;
    private Integer quantity;
    private Integer price;
}