package com.ps.paymentgatewayms.service;

import com.ps.paymentgatewayms.event.CustomEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class EventPublisherService {
    private final ApplicationEventPublisher publisher;

    public EventPublisherService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishCustomEvent(String message) {
        CustomEvent customEvent = new CustomEvent(this, message);
        publisher.publishEvent(customEvent);
    }
}

