package com.example.demo.Repository.Entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long amount;
    private String currency;

    private long userId;

    @Column(nullable = false)
    private Date timestamp;

    public Payment() {}

    public Payment( long userId,long amount, String currency) {
        this.userId = userId;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = new Date();
    }
    public long getAmount() {
        return amount;
    }
    public void setAmount(long amount) {
        this.amount = amount;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getUserId() {
        return userId;
    }




    
}
