package com.github.aniketbeez.paymentriskengine.service.interfaces;

import org.springframework.kafka.support.Acknowledgment;

public interface ConsumerInf {
    void consumeMessage(String message, Acknowledgment ack);
}
