package com.github.aniketbeez.paymentriskengine.service;

import com.github.aniketbeez.paymentriskengine.model.Payment;
import com.github.aniketbeez.paymentriskengine.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Persist payment to the database
     * @param payment
     */
    public void savePayment(Payment payment) {
        Payment savedPayment = paymentRepository.save(payment);
        log.info("payment Saved", savedPayment);
    }
}
