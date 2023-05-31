package com.github.aniketbeez.paymentwebservice.service;

import com.github.aniketbeez.paymentwebservice.domain.Payment;
import com.github.aniketbeez.paymentwebservice.model.Payee;
import com.github.aniketbeez.paymentwebservice.model.PaymentMethod;
import com.github.aniketbeez.paymentwebservice.model.PaymentUser;
import com.github.aniketbeez.paymentwebservice.repository.PayeeRepository;
import com.github.aniketbeez.paymentwebservice.repository.PaymentMethodRepository;
import com.github.aniketbeez.paymentwebservice.repository.PaymentUserRepository;
import com.github.aniketbeez.paymentwebservice.service.Interfaces.ProducerInf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.UUID;

/**
 * Service layer abstracting for the producer and the persistance layer
 */
@Slf4j
@Service
public class PaymentService {
    private final ProducerInf producer;
    private final PaymentUserRepository paymentUserRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final PayeeRepository payeeRepository;

    @Autowired
    public PaymentService(Producer producer,
                          PaymentUserRepository paymentUserRepository,
                          PaymentMethodRepository paymentMethodRepository,
                          PayeeRepository payeeRepository) {

        this.producer = producer;
        this.paymentUserRepository = paymentUserRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.payeeRepository = payeeRepository;
    }

    public String createMessage(Payment payment) throws JsonProcessingException {
        return producer.sendMessage(payment);
    }

    public void findPaymentMethodsForUser(UUID userId, List<PaymentMethod> paymentMethods) {
        paymentMethodRepository.findByUserId(userId).forEach(paymentMethods::add);
    }

    public void findPayeesForUser(UUID userId, List<PaymentUser> payees) {
        paymentUserRepository.findPayeesBy(userId).forEach(payees::add);
    }
    public PaymentUser findUserById(UUID userId) {
        return paymentUserRepository.findById(userId);
    }
    public PaymentMethod findPaymentMethodByIdAndUserId(UUID paymentMethodId, UUID userId) {
        return paymentMethodRepository.findByIdAndUserId(paymentMethodId, userId);
    }

    public Payee findPayeeForUserById(UUID userId, UUID payeeId) {
        return payeeRepository.findByUserIdAndPayeeId(userId, payeeId);
    }
}
