package com.ps.paymentgatewayms;

import com.ps.paymentgatewayms.domain.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import com.ps.paymentgatewayms.repository.PaymentRepository;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PaymentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void testSavePayment() {
        Payment payment = new Payment();
        payment.setAmount(100.0);
        payment.setCurrency("USD");
        payment.setPaymentDate(new Date());
        payment.setPaymentStatus(true);

        Payment savedPayment = paymentRepository.save(payment);
        assertThat(savedPayment).isNotNull();
        assertThat(savedPayment.getId()).isNotNull();
    }

    @Test
    public void testFindById() {
        Payment payment = new Payment();
        payment.setAmount(200.0);
        payment.setCurrency("EUR");
        payment.setPaymentDate(new Date());
        payment.setPaymentStatus(true);

        Payment savedPayment = entityManager.persistFlushFind(payment);
        Optional<Payment> foundPayment = paymentRepository.findById(savedPayment.getId());

        assertThat(foundPayment).isPresent();
        assertThat(foundPayment.get().getAmount()).isEqualTo(200.0);
    }

    @Test
    public void testDeletePayment() {
        Payment payment = new Payment();
        payment.setAmount(300.0);
        payment.setCurrency("GBP");
        payment.setPaymentDate(new Date());
        payment.setPaymentStatus(true);

        Payment savedPayment = entityManager.persistFlushFind(payment);
        paymentRepository.delete(savedPayment);

        Optional<Payment> deletedPayment = paymentRepository.findById(savedPayment.getId());
        assertThat(deletedPayment).isNotPresent();
    }
}

