package com.example.example2.dto;

public record EnrollCardResponseDTO(
        String responseCode,
        String responseCodeMessage,
        String pan
) {
}
