package com.example.example2.dto;

public record CreateCardDTO(
        String responseCode,
        String responseCodeMessage,
        int validationNumber,
        String pan
) {
}
