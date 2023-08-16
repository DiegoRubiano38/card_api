package com.example.example2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "purchase")
@Data
@Builder
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int purchaseReference;

    private int totalAmount;

    private String purchaseAddress;

    @Column(name = "transaction_timestamp")
    private long transactionTimestamp;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    @ToString.Exclude
    private Card card;
}
