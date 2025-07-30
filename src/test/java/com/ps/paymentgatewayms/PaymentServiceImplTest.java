package com.ps.paymentgatewayms;

import com.ps.paymentgatewayms.service.impl.PaymentServiceImpl;
import com.ps.paymentgatewayms.dto.PaymentRequest;
import com.ps.paymentgatewayms.repository.PaymentRepository;
import com.ps.paymentgatewayms.service.strategy.BitcoinPayment;
import com.ps.paymentgatewayms.service.strategy.CreditCardPayment;
import com.ps.paymentgatewayms.service.strategy.PayPalPayment;
import com.ps.paymentgatewayms.service.strategy.PaymentProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPayWithCreditCard() {
        PaymentRequest request = new PaymentRequest();
        request.setMethod("CreditCard");

        PaymentProcessor processor = mock(PaymentProcessor.class);
        paymentService.pay(request);

        verify(processor).setStrategy(any(CreditCardPayment.class));
        verify(processor).setPaymentRepository(paymentRepository);
        verify(processor).processPayment(request);
    }

    @Test
    public void testPayWithPayPal() {
        PaymentRequest request = new PaymentRequest();
        request.setMethod("PayPal");

        PaymentProcessor processor = mock(PaymentProcessor.class);
        paymentService.pay(request);

        verify(processor).setStrategy(any(PayPalPayment.class));
        verify(processor).setPaymentRepository(paymentRepository);
        verify(processor).processPayment(request);
    }

    @Test
    public void testPayWithBitcoin() {
        PaymentRequest request = new PaymentRequest();
        request.setMethod("Bitcoin");

        PaymentProcessor processor = mock(PaymentProcessor.class);
        paymentService.pay(request);

        verify(processor).setStrategy(any(BitcoinPayment.class));
        verify(processor).setPaymentRepository(paymentRepository);
        verify(processor).processPayment(request);
    }

    @Test
    public void testPayWithInvalidMethod() {
        PaymentRequest request = new PaymentRequest();
        request.setMethod("InvalidMethod");

        assertThrows(IllegalArgumentException.class, () -> paymentService.pay(request));
    }
}

