package com.example.example2.service;

import com.example.example2.dto.*;
import com.example.example2.entity.Card;
import com.example.example2.entity.Purchase;

import java.util.List;

public interface CardService {

    public CreateCardDTO createCard(Card card);
    public EnrollCardResponseDTO enrollCard(String pan, long validationNumber);
    public Card getCard(Long id);
    public List<Card> getAllCards ();
    public Card getCardByPan(String pan);
    public GetCardDTO getDtoCardByPan(String pan);
    public Card deleteCard(EnrollCardRequestDTO enrollCardRequestDTO);
    public CreateTransactionResponseDTO createTransaction(String pan, Purchase purchaseOrder);
    public CancelTransactionResponseDTO cancelTransaction(CancelTransactionRequestDTO cancelTransactionRequestDTO);
}
