package com.example.example2.dto;

public record CancelTransactionRequestDTO (
        String pan,
        long purchase_reference_number,
        long total_amount
){
}
