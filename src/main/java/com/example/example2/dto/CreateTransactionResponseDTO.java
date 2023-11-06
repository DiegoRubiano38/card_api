package com.example.example2.dto;

import com.example.example2.enums.TransactionStatus;

public record CreateTransactionResponseDTO (
        String response_code,
        String response_message,
        TransactionStatus transaction_status,
        int purchase_reference
){
}
