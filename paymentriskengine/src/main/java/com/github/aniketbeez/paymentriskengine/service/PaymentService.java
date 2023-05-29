package com.github.aniketbeez.paymentriskengine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aniketbeez.paymentriskengine.model.Payment;
import com.github.aniketbeez.paymentriskengine.domain.PaymentDto;
import com.github.aniketbeez.paymentriskengine.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void savePayment(Payment payment) {
        Payment savedPayment = paymentRepository.save(payment);
        log.info("payment Saved", savedPayment);
    }
}
