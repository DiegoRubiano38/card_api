package com.example.example2.dto;

public record CancelTransactionResponseDTO(
        String responseCode,
        String responseMessage,
        long purchaseReferenceNumber
){
}
