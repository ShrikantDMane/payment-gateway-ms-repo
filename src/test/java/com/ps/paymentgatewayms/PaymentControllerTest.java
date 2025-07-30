package com.ps.paymentgatewayms;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ps.paymentgatewayms.controller.PaymentController;
import com.ps.paymentgatewayms.dto.PaymentRequest;
import com.ps.paymentgatewayms.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    public void testProcessPayment_Success() throws Exception {
        when(paymentService.pay(any(PaymentRequest.class))).thenReturn(true);

        mockMvc.perform(post("/processPayment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 100.0, \"currency\": \"USD\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment processed successfully for 100.0 USD"));

        verify(paymentService).pay(any(PaymentRequest.class));
    }

    @Test
    public void testProcessPayment_Failure() throws Exception {
        when(paymentService.pay(any(PaymentRequest.class))).thenReturn(false);

        mockMvc.perform(post("/processPayment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 50.0, \"currency\": \"EUR\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment Failed"));

        verify(paymentService).pay(any(PaymentRequest.class));
    }

    @Test
    public void testProcessMent_Exception() throws Exception {
        when(paymentService.pay(any(PaymentRequest.class))).thenThrow(new RuntimeException("Service unavailable"));

        mockMvc.perform(post("/processPayment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 75.0, \"currency\": \"GBP\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Service unavailable"));

        verify(paymentService).pay(any(PaymentRequest.class));
    }
}
