package com.ps.paymentgatewayms.service;

import com.ps.paymentgatewayms.dto.PaymentRequest;

public interface PaymentService {
    boolean pay(PaymentRequest paymentRequest);
}
