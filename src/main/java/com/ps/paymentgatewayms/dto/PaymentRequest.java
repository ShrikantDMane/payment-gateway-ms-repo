package com.ps.paymentgatewayms.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String method;
    private double amount;
    private String currency;
    private UserDetails userDetails;
}

