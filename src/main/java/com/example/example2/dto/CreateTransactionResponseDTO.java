package com.example.example2.dto;

import com.example.example2.enums.TransactionStatus;

import java.math.BigInteger;

public record CreateTransactionResponseDTO (
        String responseCode,
        String responseMessage,
        TransactionStatus transactionStatus,
        int purchaseReference
){
}
