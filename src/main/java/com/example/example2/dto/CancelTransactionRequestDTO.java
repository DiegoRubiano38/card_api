package com.example.example2.dto;

public record CancelTransactionRequestDTO (
        String pan,
        long purchase_reference,
        long total_amount
){
}
