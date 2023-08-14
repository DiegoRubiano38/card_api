package com.example.example2.controller;

import com.example.example2.dto.*;
import com.example.example2.entity.Card;
import com.example.example2.enums.ResponseCode;
import com.example.example2.exceptions.ErrorMessage;
import com.example.example2.service.CardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
/*@Validated*/
@RequestMapping("/cards")
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
    public ResponseEntity<CreateCardDTO> createCard(@Valid @RequestBody Card card, BindingResult result){

        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, formatMessage(result));
        }

        CreateCardDTO cardCreated = cardService.createCard(card);

        if(cardCreated.responseCode().equals(ResponseCode.CERO_TWO.getCode())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cardCreated);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(cardCreated);
    }

    @PatchMapping(value = "/enroll")
    public ResponseEntity<EnrollCardResponseDTO> enrollCard(@RequestBody EnrollCardRequestDTO request){

        EnrollCardResponseDTO responseDTO = cardService.enrollCard(request.pan(), request.validation_number());

        if(responseDTO.responseCode().equals(ResponseCode.CERO_CERO.getCode())){
            return ResponseEntity.ok(responseDTO);
        } else if(responseDTO.responseCode().equals(ResponseCode.CERO_ONE.getCode())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        } else if(responseDTO.responseCode().equals(ResponseCode.CERO_TWO.getCode())){
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
    public ResponseEntity<CreateTransactionResponseDTO> createPurchase(@RequestBody CreateTransactionRequestDTO request){

        CreateTransactionResponseDTO createTransactionResponseDTO = cardService.createTransaction(request.pan(), request.purchase_order());

        if(createTransactionResponseDTO.responseCode().equals(ResponseCode.CERO_ONE.getCode())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createTransactionResponseDTO);
        } else if(createTransactionResponseDTO.responseCode().equals(ResponseCode.CERO_TWO.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createTransactionResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(createTransactionResponseDTO);
        }
    }

    @PutMapping(value = "/cancel_purchase")
    public ResponseEntity<CancelTransactionResponseDTO> cancelPurchase(@RequestBody CancelTransactionRequestDTO cancelTransactionRequestDTO){

        CancelTransactionResponseDTO cancelTransactionResponseDTO = cardService.cancelTransaction(cancelTransactionRequestDTO);

        if(cancelTransactionResponseDTO.responseCode().equals(ResponseCode.CERO_ONE.getCode())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cancelTransactionResponseDTO);
        } else if(cancelTransactionResponseDTO.responseCode().equals(ResponseCode.CERO_TWO.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cancelTransactionResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(cancelTransactionResponseDTO);
        }
    }

    private String formatMessage(BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String>  error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).toList();
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();

        ObjectMapper mapper = new ObjectMapper();
        String jsonString;

        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonString;
    }
}
