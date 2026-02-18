package com.example.demo.Repository.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long amount;
    private String currency;

    private long userId;

    public Payment() {}

    public Payment( long userId,long amount, String currency) {
        this.userId = userId;
        this.amount = amount;
        this.currency = currency;
    }


    
}
