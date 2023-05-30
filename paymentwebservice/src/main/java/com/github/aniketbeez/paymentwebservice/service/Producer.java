package com.github.aniketbeez.paymentwebservice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aniketbeez.paymentwebservice.domain.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class Producer {

    @Value("${topic.name}")
    private String paymentTopic;

    private final ObjectMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Autowired
    public Producer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }

    public String sendMessage(Payment payment) throws JsonProcessingException {
        String paymentMessage = mapper.writeValueAsString(payment);
        kafkaTemplate.send(paymentTopic, paymentMessage);

        log.info("payment message produced {}", paymentMessage);

        return "Payment created!";
    }
}
