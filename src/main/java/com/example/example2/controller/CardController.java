package com.example.example2.controller;

import com.example.example2.dto.*;
import com.example.example2.entity.Card;
import com.example.example2.enums.ResponseCode;
import com.example.example2.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
/*@Validated*/
@RequestMapping("api/v1/cards")
public class CardController {
    CardService cardService;
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
    @GetMapping(value = "/")
    public ResponseEntity<GetCardDTO> getCard(@RequestParam(value = "pan") String pan){

        if (pan.startsWith("\"") && pan.endsWith("\"")) {
            pan = pan.substring(1, pan.length() - 1);
        }

        GetCardDTO card = cardService.getDtoCardByPan(pan);
        if(null == card){return ResponseEntity.notFound().build();}

        return ResponseEntity.ok(card);
    }

    @GetMapping(value = "/get_all_cards")
    public ResponseEntity<List<Card>> getAllCards(){

        List<Card> allCards = cardService.getAllCards();
        return ResponseEntity.ok(allCards);
    }

    @PostMapping (value = "/create")
    public ResponseEntity<CreateCardDTO> createCard(@Valid @RequestBody Card card){

        CreateCardDTO cardCreated = cardService.createCard(card);

        if(cardCreated.response_code().equals(ResponseCode.CERO_TWO.getCode())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cardCreated);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(cardCreated);
    }

    @PatchMapping(value = "/enroll")
    public ResponseEntity<EnrollCardResponseDTO> enrollCard(@RequestBody EnrollCardRequestDTO request){

        EnrollCardResponseDTO responseDTO = cardService.enrollCard(request.pan(), request.validation_number());

        if(responseDTO.response_code().equals(ResponseCode.CERO_CERO.getCode())){
            return ResponseEntity.ok(responseDTO);
        } else if(responseDTO.response_code().equals(ResponseCode.CERO_ONE.getCode())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        } else if(responseDTO.response_code().equals(ResponseCode.CERO_TWO.getCode())){
            return ResponseEntity.badRequest().body(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<ResponseCodeDTO> deleteCard(@RequestBody EnrollCardRequestDTO request){

        Card card = cardService.deleteCard(request);
        ResponseCode responseCode;

        if(card == null){
            responseCode = ResponseCode.CERO_TWO;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseCodeDTO(responseCode.getCode(), responseCode.getDeleteMethodMessage()));
        } else if(request.validation_number() != card.getValidationNumber()){
            responseCode = ResponseCode.CERO_ONE;
            return ResponseEntity.badRequest().body(new ResponseCodeDTO(responseCode.getCode(), responseCode.getDeleteMethodMessage()));
        } else {
            responseCode = ResponseCode.CERO_CERO;
            return ResponseEntity.ok(new ResponseCodeDTO(responseCode.getCode(), responseCode.getDeleteMethodMessage()));
        }
    }

    @PostMapping(value = "/create_purchase")
    public ResponseEntity<CreateTransactionResponseDTO> createPurchase(@Valid @RequestBody CreateTransactionRequestDTO request){

        CreateTransactionResponseDTO createTransactionResponseDTO = cardService.createTransaction(request.pan(), request.purchase_order());

        if(createTransactionResponseDTO.response_code().equals(ResponseCode.CERO_ONE.getCode())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createTransactionResponseDTO);
        } else if(createTransactionResponseDTO.response_code().equals(ResponseCode.CERO_TWO.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createTransactionResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(createTransactionResponseDTO);
        }
    }

    @PutMapping(value = "/cancel_purchase")
    public ResponseEntity<CancelTransactionResponseDTO> cancelPurchase(@RequestBody CancelTransactionRequestDTO cancelTransactionRequestDTO){

        CancelTransactionResponseDTO cancelTransactionResponseDTO = cardService.cancelTransaction(cancelTransactionRequestDTO);

        if(cancelTransactionResponseDTO.response_code().equals(ResponseCode.CERO_ONE.getCode())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cancelTransactionResponseDTO);
        } else if(cancelTransactionResponseDTO.response_code().equals(ResponseCode.CERO_TWO.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cancelTransactionResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(cancelTransactionResponseDTO);
        }
    }
}
