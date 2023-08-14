package com.example.example2.dto;

import com.example.example2.entity.Purchase;

public record CreateTransactionRequestDTO (
        String pan,
        Purchase purchase_order
){
}
