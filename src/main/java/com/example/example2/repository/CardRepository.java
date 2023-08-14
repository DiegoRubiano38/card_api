package com.example.example2.repository;

import com.example.example2.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

    public Card findByPan(String pan);
}
