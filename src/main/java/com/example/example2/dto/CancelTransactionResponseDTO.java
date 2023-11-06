package com.example.example2.dto;

public record CancelTransactionResponseDTO(
        String response_code,
        String response_message,
        long purchase_reference_number
){
}
