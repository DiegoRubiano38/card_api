package com.example.example2.entity;

import com.example.example2.enums.CardStatus;
import com.example.example2.enums.CardType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "card")
@Data
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Pattern(regexp = "^[0-9]{16,19}$", message = "Card number must have 16 to 19 digits")
    private String pan;
    private String customer;
    private long identification;
    @Column(name = "card_type")
    private CardType cardType;
    @Size(min = 10, max = 10, message = "Phone number should contain exactly 10 digits.")
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "card_status")
    private CardStatus cardStatus;
    @Column(name = "validation_number")
    private int validationNumber;
    @OneToMany(mappedBy = "card")
    private List<Purchase> purchases;

}
