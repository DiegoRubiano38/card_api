package com.example.example2.service.impl;
import com.example.example2.dto.*;
import com.example.example2.entity.Card;
import com.example.example2.entity.Purchase;
import com.example.example2.enums.CardStatus;
import com.example.example2.enums.ResponseCode;
import com.example.example2.enums.TransactionStatus;
import com.example.example2.repository.CardRepository;
import com.example.example2.repository.PurchaseRepository;
import com.example.example2.service.CardService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final PurchaseRepository purchaseRepository;
    private final Random random = new Random();

    public CardServiceImpl(CardRepository cardRepository, PurchaseRepository purchaseRepository) {
        this.cardRepository = cardRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public CreateCardDTO createCard(Card card) {

        int validationNumber;
        Card saveCard;
        ResponseCode responseCode;

        if(isPanExists(card.getPan())){
            responseCode = ResponseCode.CERO_TWO;
            return new CreateCardDTO(responseCode.getCode(), responseCode.getCreateMethodMessage(), 0, maskPan(card.getPan()));
        }

        validationNumber = random.nextInt(100) + 1;
        card.setValidationNumber(validationNumber);
        card.setCardStatus(CardStatus.CREATED);
        saveCard = cardRepository.save(card);

        if(0 != saveCard.getId()){
            responseCode = ResponseCode.CERO_CERO;
        } else {
            responseCode = ResponseCode.CERO_ONE;
        }

        return new CreateCardDTO(responseCode.getCode(), responseCode.getCreateMethodMessage(), saveCard.getValidationNumber(), maskPan(saveCard.getPan()));
    }

    @Override
    public EnrollCardResponseDTO enrollCard(String pan, long validationNumber) {

        ResponseCode responseCode;

        Card cardToEnroll = getCardByPan(pan);

        if(cardToEnroll == null){
            responseCode = ResponseCode.CERO_ONE;
        } else if(cardToEnroll.getValidationNumber() == validationNumber){
            cardToEnroll.setCardStatus(CardStatus.ENROLLED);
            responseCode = ResponseCode.CERO_CERO;
            cardRepository.save(cardToEnroll);
        } else {
            responseCode = ResponseCode.CERO_TWO;
        }

        return new EnrollCardResponseDTO(responseCode.getCode(), responseCode.getEnrollMethodMessage(), maskPan(pan));
    }

    @Override
    public List<Card> getAllCards (){
        return cardRepository.findAll();
    }
    @Override
    public Card getCard(Long id) {
        return cardRepository.findById(id).orElse(null);
    }
    @Override
    public GetCardDTO getDtoCardByPan(String pan) {

        Card card = cardRepository.findByPan(pan);

        if (card == null) {return null;}

        return mapToGetCardDTO(card);
    }
    @Override
    public Card getCardByPan(String pan) {
        return cardRepository.findByPan(pan);
    }

    @Override
    public Card deleteCard(EnrollCardRequestDTO enrollCardRequestDTO) {

        Card cardDb = getCardByPan(enrollCardRequestDTO.pan());

        if(null == cardDb){return null;}
        else if (cardDb.getValidationNumber() != enrollCardRequestDTO.validation_number()) {
            return new Card();
        }

        cardDb.setCardStatus(CardStatus.DELETED);

        return cardRepository.save(cardDb);
    }

    @Override
    public CreateTransactionResponseDTO createTransaction(String pan, Purchase purchaseOrder) {

        Card cardDb = getCardByPan(pan);
        ResponseCode responseCode;
        TransactionStatus transactionStatus;
        LocalTime timestamp = LocalTime.now();

        if(null == cardDb) {

            responseCode = ResponseCode.CERO_ONE;
            transactionStatus = TransactionStatus.REJECTED;
            purchaseOrder.setPurchaseReference(0);
        }


        else if (cardDb.getCardStatus().equals(CardStatus.ENROLLED)){

            responseCode = ResponseCode.CERO_CERO;
            transactionStatus = TransactionStatus.APPROVED;
            purchaseOrder.setPurchaseReference(random.nextInt(999999) + 1);
            purchaseOrder.setTransactionTimestamp(timestamp);
            purchaseOrder.setCard(cardDb);

            purchaseRepository.save(purchaseOrder);
            cardRepository.save(cardDb);

        } else {

            responseCode = ResponseCode.CERO_TWO;
            transactionStatus = TransactionStatus.REJECTED;
            purchaseOrder.setPurchaseReference(0);

        }

        return new CreateTransactionResponseDTO(
                responseCode.getCode(),
                responseCode.getCreateTransactionMethodMessage(),
                transactionStatus,
                purchaseOrder.getPurchaseReference());
    }

    @Override
    public CancelTransactionResponseDTO cancelTransaction(CancelTransactionRequestDTO cancelTransactionRequestDTO) {

        ResponseCode responseCode;
        Optional<Card> card = Optional.ofNullable(cardRepository.findByPan(cancelTransactionRequestDTO.pan()));

        if(card.isEmpty()){
            responseCode = ResponseCode.CERO_TWO;
        } else {
            Card foundCard = card.get();
            Optional<Purchase> matchingPurchase = foundCard.getPurchases().stream()
                    .filter(purchase -> purchase.getPurchaseReference() == cancelTransactionRequestDTO.purchase_reference_number())
                    .findFirst();

            if (matchingPurchase.isPresent()) {
                Purchase foundPurchase = matchingPurchase.get();
                if(foundPurchase.getTransactionTimestamp().plusMinutes(5).isBefore(LocalTime.now())){
                    responseCode = ResponseCode.CERO_ONE;
                } else {
                    responseCode = ResponseCode.CERO_CERO;
                    purchaseRepository.delete(foundPurchase);
                }

            } else {
                responseCode = ResponseCode.CERO_ONE;
            }
        }
        return new CancelTransactionResponseDTO(responseCode.getCode(),
                responseCode.getCancelTransactionMethodMessage(),
                cancelTransactionRequestDTO.purchase_reference_number());
    }

    private String maskPan(String pan){

        int panLengthMod = pan.length() - 10;

        return pan.substring(0, 6) + String.join("", Collections.nCopies(panLengthMod, "*")) + pan.substring(pan.length() - 4);
    }

    private GetCardDTO mapToGetCardDTO(Card card) {
        return new GetCardDTO(
                maskPan(card.getPan()),
                card.getCustomer(),
                card.getIdentification(),
                card.getPhoneNumber(),
                card.getCardStatus()
        );
    }

    boolean isPanExists(String pan){
        return (cardRepository.findByPan(pan) != null);
    }
}
