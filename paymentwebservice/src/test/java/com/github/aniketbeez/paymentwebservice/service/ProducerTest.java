package com.github.aniketbeez.paymentwebservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aniketbeez.paymentwebservice.domain.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProducerTest {

    private String paymentTopic;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private Producer producer;

    @BeforeEach
    public void setup() {
        this.paymentTopic = "testTopic";
    }

    @Test
    public void testSendMessage() throws JsonProcessingException {
        Payment payment = mock(Payment.class);
        String testMessageString = "testMessageString";
        when(mapper.writeValueAsString(payment)).thenReturn(testMessageString);
        when(kafkaTemplate.send(null, testMessageString)).thenReturn(null);
        assertEquals(producer.sendMessage(payment), "message sent");
    }

    @Test
    public void testSendMessage_error() throws JsonProcessingException {
        Payment payment = mock(Payment.class);
        when(mapper.writeValueAsString(payment)).thenThrow(JsonProcessingException.class);
        assertThrows(JsonProcessingException.class, () -> producer.sendMessage(payment));
    }
}
