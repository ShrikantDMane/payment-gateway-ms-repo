package com.ps.paymentgatewayms.event;

import com.ps.paymentgatewayms.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CustomEventListener {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    @EventListener
    public void onApplicationEvent(CustomEvent event) {
      logger.info("Received custom event - {}" , event.getMessage());
    }
}

