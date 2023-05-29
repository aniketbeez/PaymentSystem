package com.github.aniketbeez.paymentwebservice.utils;

import com.github.aniketbeez.paymentwebservice.domain.Payment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PaymentValidatorTest {

    @InjectMocks
    private PaymentValidator paymentValidator;

    @Test
    public void testIsValidPaymentRequest_NullAmount() {
        Payment paymentRequest = new Payment();
        paymentRequest.setPayeeId("019b6d88-fd17-11ed-be56-0242ac120002");
        paymentRequest.setUserId("019b7152-fd17-11ed-be56-0242ac120002");
        paymentRequest.setPaymentMethodId("019b736e-fd17-11ed-be56-0242ac120002");
        paymentRequest.setCurrency("USD");

        var response = paymentValidator.isValidPaymentRequest(paymentRequest);
        assertEquals(response, false);

    }

    @Test
    public void testIsValidPaymentRequest_NullPaymentMethodId() {
        Payment paymentRequest = new Payment();
        paymentRequest.setPayeeId("019b6d88-fd17-11ed-be56-0242ac120002");
        paymentRequest.setAmount(new BigDecimal(10));
        paymentRequest.setUserId("019b736e-fd17-11ed-be56-0242ac120002");
        paymentRequest.setCurrency("USD");

        var response = paymentValidator.isValidPaymentRequest(paymentRequest);
        assertEquals(response, false);

    }

    @Test
    public void testIsValidPaymentRequest_NullCurrency() {
        Payment paymentRequest = new Payment();
        paymentRequest.setPayeeId("019b6d88-fd17-11ed-be56-0242ac120002");
        paymentRequest.setUserId("019b7152-fd17-11ed-be56-0242ac120002");
        paymentRequest.setPaymentMethodId("019b736e-fd17-11ed-be56-0242ac120002");
        paymentRequest.setAmount(new BigDecimal(10));

        var response = paymentValidator.isValidPaymentRequest(paymentRequest);
        assertEquals(response, false);

    }

    @Test
    public void testIsValidPaymentRequest_NullUserId() {
        Payment paymentRequest = new Payment();
        paymentRequest.setPayeeId("019b6d88-fd17-11ed-be56-0242ac120002");
        paymentRequest.setAmount(new BigDecimal(10));
        paymentRequest.setPaymentMethodId("019b736e-fd17-11ed-be56-0242ac120002");
        paymentRequest.setCurrency("USD");

        var response = paymentValidator.isValidPaymentRequest(paymentRequest);
        assertEquals(response, false);

    }

    @Test
    public void testIsValidPaymentRequest_NullPayeeId() {
        Payment paymentRequest = new Payment();
        paymentRequest.setUserId("019b6d88-fd17-11ed-be56-0242ac120002");
        paymentRequest.setAmount(new BigDecimal(10));
        paymentRequest.setPaymentMethodId("019b736e-fd17-11ed-be56-0242ac120002");
        paymentRequest.setCurrency("USD");

        var response = paymentValidator.isValidPaymentRequest(paymentRequest);
        assertEquals(response, false);

    }

    @Test
    public void testIsValidPaymentRequest_Success() {
        Payment paymentRequest = new Payment();
        paymentRequest.setUserId("019b6d88-fd17-11ed-be56-0242ac120002");
        paymentRequest.setPayeeId("019b6d88-fd17-11ed-be56-0242ac120002");
        paymentRequest.setAmount(new BigDecimal(10));
        paymentRequest.setPaymentMethodId("019b736e-fd17-11ed-be56-0242ac120002");
        paymentRequest.setCurrency("USD");

        var response = paymentValidator.isValidPaymentRequest(paymentRequest);
        assertEquals(response, true);

    }

    @Test
    public void testIsValidCurrency_Failure() {
        String testCurrency = "testCurrency";
        var response = paymentValidator.isValidCurrency(testCurrency);
        assertEquals(response, false);
    }
    @Test
    public void testIsValidCurrency_Success() {
        String testCurrency = "USD";
        var response = paymentValidator.isValidCurrency(testCurrency);
        assertEquals(response, true);
    }

    @Test
    public void testIsValidEmail_Failure() {
        String email = "testEmail";
        var response = paymentValidator.isValidEmail(email);
        assertEquals(response, false);
    }

    @Test
    public void testIsValidEmail_Success() {
        String email = "abc@def.com";
        var response = paymentValidator.isValidEmail(email);
        assertEquals(response, true);
    }

    @Test
    public void testIsValidUuid_Failure() {
        String uuid = "testUuid";
        var response = paymentValidator.isValidUuid(uuid);
        assertEquals(response, false);
    }

    @Test
    public void testIsValidUuid_Success() {
        String uuid = "019b736e-fd17-11ed-be56-0242ac120002";
        var response = paymentValidator.isValidUuid(uuid);
        assertEquals(response, true);
    }
}
