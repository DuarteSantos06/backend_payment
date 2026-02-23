package com.example.demo.Service.DTO;

import java.util.Date;

public record PaymentDTO(long id, long amount, String currency, Date timestamp) {
}

