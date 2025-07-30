package com.ps.paymentgatewayms.controller;

import com.ps.paymentgatewayms.dto.PaymentRequest;
import com.ps.paymentgatewayms.service.CacheService;
import com.ps.paymentgatewayms.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private CacheService cacheService;

    @PostMapping("/processPayment")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest, @RequestHeader Map<String, Object> header) throws Exception {
        logger.info("Processing payment request : {}", paymentRequest);
        User u = (User) header.get("USER");
        cacheService.cacheUserDetails("123", u );
        boolean status =  paymentService.pay(paymentRequest);
        return ResponseEntity.ok(status ? "Payment processed successfully for " + paymentRequest.getAmount() + " " + paymentRequest.getCurrency() : "Payment Failed");
    }
}
