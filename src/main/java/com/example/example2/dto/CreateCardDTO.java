package com.example.example2.dto;

public record CreateCardDTO(
        String response_code,
        String response_code_message,
        int validation_number,
        String pan
) {
}
