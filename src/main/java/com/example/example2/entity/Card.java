package com.example.example2.entity;

import com.example.example2.enums.CardStatus;
import com.example.example2.enums.CardType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@Builder
@Table(name = "card")
@Data
public class Card {

    public Card() {
        // Empty constructor to deserialize object
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Pattern(regexp = "^[0-9]{16,19}$", message = "Card number must have 16 to 19 digits")
    private String pan;

    @NotBlank
    @NotEmpty(message = "Customer must not be empty")
    private String customer;

    @NotNull(message = "Identification must not be empty")
    private long identification;

    @NotNull(message = "Cannot be empty")
    @Column(name = "card_type")
    private CardType cardType;

    @Size(min = 10, max = 10, message = "Phone number should contain exactly 10 digits.")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "card_status")
    private CardStatus cardStatus;

    @Column(name = "validation_number")
    private int validationNumber;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Purchase> purchases;

}
