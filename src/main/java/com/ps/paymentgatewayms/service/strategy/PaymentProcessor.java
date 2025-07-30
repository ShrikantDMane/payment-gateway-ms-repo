package com.ps.paymentgatewayms.service.strategy;

import com.ps.paymentgatewayms.domain.Payment;
import com.ps.paymentgatewayms.dto.PaymentRequest;
import com.ps.paymentgatewayms.repository.PaymentRepository;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@NoArgsConstructor
public class PaymentProcessor {

    private PaymentStrategy strategy;

    private PaymentRepository paymentRepository;

    public Payment processPayment(PaymentRequest paymentRequest) {
        strategy.processPayment(paymentRequest.getAmount());
        Payment p = new Payment();
        p.setPaymentDate(new Date());
        p.setPaymentStatus(true);
        p.setAmount(paymentRequest.getAmount());
        p.setCurrency(paymentRequest.getCurrency());
        p.setMethod(paymentRequest.getMethod());
        return paymentRepository.saveAndFlush(p);
    }

}

