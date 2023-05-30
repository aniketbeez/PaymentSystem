package com.github.aniketbeez.paymentriskengine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aniketbeez.paymentriskengine.model.Payment;
import com.github.aniketbeez.paymentriskengine.domain.PaymentDto;
import com.github.aniketbeez.paymentriskengine.service.PaymentService;
import com.github.aniketbeez.paymentriskengine.service.interfaces.ConsumerInf;
import com.github.aniketbeez.paymentriskengine.service.interfaces.RiskCalculatorInf;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer implements ConsumerInf {
    private static final String paymentTopic = "t.payments";

    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RiskCalculatorInf pocRiskCalculator;
    private final PaymentCounter paymentCounter;

    @Autowired
    public Consumer(ObjectMapper objectMapper, ModelMapper modelMapper, PaymentService paymentService,
                    PocRiskCalculator pocRiskCalculator, PaymentCounter paymentCounter) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.paymentService = paymentService;
        this.pocRiskCalculator = pocRiskCalculator;
        this.paymentCounter = paymentCounter;
    }

    @KafkaListener(topics = paymentTopic)
    public void consumeMessage(String message, Acknowledgment ack) {
        log.info("message consumed : {}", message);
        try {
            PaymentDto paymentDto = objectMapper.readValue(message, PaymentDto.class);
            Payment payment = modelMapper.map(paymentDto, Payment.class);
            int riskScore = pocRiskCalculator.calculateRiskScore(payment);
            payment.setRiskScore(riskScore);
            payment.setApproved(isValidPayment(payment));

            log.info("Persisting message to DB :" + payment);
            paymentService.savePayment(payment);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Consumer Exception :" + e.getStackTrace());
        }
    }

    private boolean isValidPayment(Payment payment) {
        if(paymentCounter.getApprovedPaymentCounter().get() < 7) {
            paymentCounter.getApprovedPaymentCounter().incrementAndGet();
            return true;
        } else {
            if(paymentCounter.getRejectedPaymentCounter().incrementAndGet() == 3)
                paymentCounter.getApprovedPaymentCounter().set(0);
            return false;
        }
    }

}
