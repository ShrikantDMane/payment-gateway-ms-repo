package com.ps.paymentgatewayms.service.impl;

import com.ps.paymentgatewayms.dto.PaymentRequest;
import com.ps.paymentgatewayms.exception.PaymentProcessingException;
import com.ps.paymentgatewayms.repository.PaymentRepository;
import com.ps.paymentgatewayms.service.CacheService;
import com.ps.paymentgatewayms.service.PaymentService;
import com.ps.paymentgatewayms.service.strategy.BitcoinPayment;
import com.ps.paymentgatewayms.service.strategy.CreditCardPayment;
import com.ps.paymentgatewayms.service.strategy.PayPalPayment;
import com.ps.paymentgatewayms.service.strategy.PaymentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private CacheService cacheService;

    @Value("${notification.queue}")
    private String notificationQueue;

    private ExecutorService executor = Executors.newCachedThreadPool();

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Override
    public boolean pay(PaymentRequest paymentRequest) {

        UserDetails user = cacheService.getUserDetails("123");

        PaymentProcessor paymentProcessor = new PaymentProcessor();
        switch (paymentRequest.getMethod()){
            case "CreditCard" -> paymentProcessor.setStrategy(new CreditCardPayment());
            case "PayPal" -> paymentProcessor.setStrategy(new PayPalPayment());
            case "Bitcoin" -> paymentProcessor.setStrategy(new BitcoinPayment());
            default -> throw new PaymentProcessingException("Invalid method");
        }
        paymentProcessor.setPaymentRepository(paymentRepository);

        CompletableFuture.supplyAsync(() -> {
            logger.info("Processing payment request for user : {}", user.getUsername());
            cacheService.deleteUserDetails("123");
            return  paymentProcessor.processPayment(paymentRequest);
        }, executor).thenAccept(preparedMessage -> {
            jmsTemplate.convertAndSend(notificationQueue, "SUCCESS");
            logger.info("Message sent asynchronously: {}", "SUCCESS");
        });

        return true;
    }

}
