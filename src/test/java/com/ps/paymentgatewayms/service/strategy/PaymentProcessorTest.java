package com.ps.paymentgatewayms.service.strategy;

import static org.junit.jupiter.api.Assertions.*;
import com.ps.paymentgatewayms.service.strategy.PaymentProcessor;

import com.ps.paymentgatewayms.domain.Payment;
import com.ps.paymentgatewayms.dto.PaymentRequest;
import com.ps.paymentgatewayms.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Date;
import static org.mockito.Mockito.*;

public class PaymentProcessorTest {

    @Mock
    private PaymentStrategy paymentStrategy;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentProcessor paymentProcessor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        paymentProcessor.setStrategy(paymentStrategy);
        paymentProcessor.setPaymentRepository(paymentRepository);
    }

    @Test
    public void testProcessPayment() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(100.0);
        paymentRequest.setCurrency("USD");
        paymentRequest.setMethod("CreditCard");

        paymentProcessor.processPayment(paymentRequest);

        verify(paymentStrategy).processPayment(paymentRequest.getAmount());
        verify(paymentRepository).saveAndFlush(any(Payment.class));
    }

    @Test
    public void testPaymentDetails() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(150.0);
        paymentRequest.setCurrency("EUR");
        paymentRequest.setMethod("PayPal");

        paymentProcessor.processPayment(paymentRequest);

        verify(paymentRepository).saveAndFlush(argThat(payment ->
                payment.getAmount().equals(150.0) &&
                        payment.getCurrency().equals("EUR") &&
                        payment.getMethod().equals("PayPal") &&
                        payment.getPaymentStatus() &&
                        payment.getPaymentDate() != null
        ));
    }
}
