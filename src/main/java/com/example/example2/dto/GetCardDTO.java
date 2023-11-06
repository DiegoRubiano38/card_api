package com.example.example2.dto;

import com.example.example2.enums.CardStatus;

public record GetCardDTO(
        String pan,
        String customer,
        long identification,
        String phone_number,
        CardStatus card_status) {
}
