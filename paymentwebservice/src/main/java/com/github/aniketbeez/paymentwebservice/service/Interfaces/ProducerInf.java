package com.github.aniketbeez.paymentwebservice.service.Interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.aniketbeez.paymentwebservice.domain.Payment;

public interface ProducerInf {
    String sendMessage(Payment payment) throws JsonProcessingException;
}
