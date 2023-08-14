package com.example.example2.service.impl;

import com.example.example2.entity.Purchase;
import com.example.example2.repository.PurchaseRepository;
import com.example.example2.service.PurchaseService;

public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public Purchase savePurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }
}
