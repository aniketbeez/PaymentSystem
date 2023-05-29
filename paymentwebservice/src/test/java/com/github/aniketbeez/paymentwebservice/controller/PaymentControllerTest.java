package com.github.aniketbeez.paymentwebservice.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.aniketbeez.paymentwebservice.domain.PayeeResponse;
import com.github.aniketbeez.paymentwebservice.domain.Payment;
import com.github.aniketbeez.paymentwebservice.domain.PaymentMethodResponse;
import com.github.aniketbeez.paymentwebservice.domain.PaymentResponse;
import com.github.aniketbeez.paymentwebservice.exceptions.InvalidPaymentException;
import com.github.aniketbeez.paymentwebservice.model.Payee;
import com.github.aniketbeez.paymentwebservice.model.PaymentMethod;
import com.github.aniketbeez.paymentwebservice.model.PaymentUser;
import com.github.aniketbeez.paymentwebservice.service.PaymentService;
import com.github.aniketbeez.paymentwebservice.utils.PaymentValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {
    @Mock
    private PaymentService paymentService;

    @Mock
    private PaymentValidator paymentValidator;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    public void testCreatePayment_nullOrBlankRequestParamater() {
        Payment payment = mock(Payment.class);
        when(paymentValidator.isValidPaymentRequest(payment)).thenReturn(false);
        ResponseEntity<PaymentResponse> response = paymentController.createPayment(payment);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getErrorMessage(),"Please provide all required fields!");
    }

    @Test
    public void testCreatePayment_invalidCurrency() {
        Payment payment = mock(Payment.class);
        payment.setCurrency("test");
        when(paymentValidator.isValidPaymentRequest(payment)).thenReturn(true);
        ResponseEntity<PaymentResponse> response = paymentController.createPayment(payment);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getErrorMessage(),"Please provide valid currency code!");
    }

    @Test
    public void testCreatePayment_invalidUserId() {
        Payment payment = createTestPayment();
        when(paymentValidator.isValidPaymentRequest(payment)).thenReturn(true);
        when(paymentValidator.isValidCurrency(anyString())).thenReturn(true);
        when(paymentValidator.isValidUuid(anyString())).thenReturn(false);
        ResponseEntity<PaymentResponse> response = paymentController.createPayment(payment);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getErrorMessage(),"User Id is not a valid format!");
    }

    @Test
    public void testCreatePayment_userIdNotRegistered() {
        Payment payment = createTestPayment();
        when(paymentValidator.isValidPaymentRequest(payment)).thenReturn(true);
        when(paymentValidator.isValidCurrency(anyString())).thenReturn(true);
        when(paymentValidator.isValidUuid(anyString())).thenReturn(true);
        when(paymentService.findUserById(UUID.fromString(payment.getUserId()))).thenReturn(null);
        ResponseEntity<PaymentResponse> response = paymentController.createPayment(payment);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getErrorMessage(),"Provided user is not a registered user!");
    }

    @Test
    public void testCreatePayment_payeeNotRegisteredForUser() {
        Payment payment = createTestPayment();
        PaymentUser user = mock(PaymentUser.class);
        PaymentUser payee = mock(PaymentUser.class);

        when(paymentValidator.isValidPaymentRequest(payment)).thenReturn(true);
        when(paymentValidator.isValidCurrency(anyString())).thenReturn(true);
        when(paymentValidator.isValidUuid(anyString())).thenReturn(true);
        when(paymentService.findUserById(UUID.fromString(payment.getUserId()))).thenReturn(user);
        when(paymentService.findUserById(UUID.fromString(payment.getPayeeId()))).thenReturn(payee);
        when(paymentService.findPayeeForUserById(UUID.fromString(payment.getUserId()),
                UUID.fromString(payment.getPayeeId()))).thenReturn(null);
        ResponseEntity<PaymentResponse> response = paymentController.createPayment(payment);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getErrorMessage(),"Provided payee is not registered for this user!");
    }

    @Test
    public void testCreatePayment_invalidPaymentMethodId() {
        Payment payment = createTestPayment();
        PaymentUser user = mock(PaymentUser.class);
        PaymentUser payee = mock(PaymentUser.class);

        when(paymentValidator.isValidPaymentRequest(payment)).thenReturn(true);
        when(paymentValidator.isValidCurrency(anyString())).thenReturn(true);
        when(paymentValidator.isValidUuid(anyString())).thenReturn(true);
        when(paymentService.findUserById(UUID.fromString(payment.getUserId()))).thenReturn(user);
        when(paymentService.findUserById(UUID.fromString(payment.getPayeeId()))).thenReturn(payee);
        when(paymentService.findPayeeForUserById(UUID.fromString(payment.getUserId()),
                UUID.fromString(payment.getPayeeId()))).thenReturn(mock(Payee.class));
        when(paymentValidator.isValidUuid(payment.getPaymentMethodId())).thenReturn(false);
        ResponseEntity<PaymentResponse> response = paymentController.createPayment(payment);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getErrorMessage(),"Payment method id is invalid format!");
    }

    @Test
    public void testCreatePayment_PaymentMethodNotRegisteredForuser() {
        Payment payment = createTestPayment();
        PaymentUser user = mock(PaymentUser.class);
        PaymentUser payee = mock(PaymentUser.class);

        when(paymentValidator.isValidPaymentRequest(payment)).thenReturn(true);
        when(paymentValidator.isValidCurrency(anyString())).thenReturn(true);
        when(paymentValidator.isValidUuid(anyString())).thenReturn(true);
        when(paymentService.findUserById(UUID.fromString(payment.getUserId()))).thenReturn(user);
        when(paymentService.findUserById(UUID.fromString(payment.getPayeeId()))).thenReturn(payee);
        when(paymentService.findPayeeForUserById(UUID.fromString(payment.getUserId()),
                UUID.fromString(payment.getPayeeId()))).thenReturn(mock(Payee.class));
        when(paymentValidator.isValidUuid(payment.getPaymentMethodId())).thenReturn(true);
        when(paymentService.findPaymentMethodByIdAndUserId(UUID.fromString(payment.getPaymentMethodId()),
                UUID.fromString(payment.getUserId()))).thenReturn(null);
        ResponseEntity<PaymentResponse> response = paymentController.createPayment(payment);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getErrorMessage(),"Provided payment method is not registered for this user!");
    }

    @Test
    public void testCreatePayment_ThrowsException() throws JsonProcessingException {
        Payment payment = createTestPayment();
        PaymentUser user = mock(PaymentUser.class);
        PaymentUser payee = mock(PaymentUser.class);

        when(paymentValidator.isValidPaymentRequest(payment)).thenReturn(true);
        when(paymentValidator.isValidCurrency(anyString())).thenReturn(true);
        when(paymentValidator.isValidUuid(anyString())).thenReturn(true);
        when(paymentService.findUserById(UUID.fromString(payment.getUserId()))).thenReturn(user);
        when(paymentService.findUserById(UUID.fromString(payment.getPayeeId()))).thenReturn(payee);
        when(paymentService.findPayeeForUserById(UUID.fromString(payment.getUserId()),
                UUID.fromString(payment.getPayeeId()))).thenReturn(mock(Payee.class));
        when(paymentValidator.isValidUuid(payment.getPaymentMethodId())).thenReturn(true);
        when(paymentService.findPaymentMethodByIdAndUserId(UUID.fromString(payment.getPaymentMethodId()),
                UUID.fromString(payment.getUserId()))).thenReturn(mock(PaymentMethod.class));
        when(paymentService.createMessage(payment)).thenThrow(JsonProcessingException.class);
        ResponseEntity<PaymentResponse> response = paymentController.createPayment(payment);
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Test
    public void testCreatePayment_Success() throws JsonProcessingException {
        Payment payment = createTestPayment();
        PaymentUser user = mock(PaymentUser.class);
        PaymentUser payee = mock(PaymentUser.class);

        when(paymentValidator.isValidPaymentRequest(payment)).thenReturn(true);
        when(paymentValidator.isValidCurrency(anyString())).thenReturn(true);
        when(paymentValidator.isValidUuid(anyString())).thenReturn(true);
        when(paymentService.findUserById(UUID.fromString(payment.getUserId()))).thenReturn(user);
        when(paymentService.findUserById(UUID.fromString(payment.getPayeeId()))).thenReturn(payee);
        when(paymentService.findPayeeForUserById(UUID.fromString(payment.getUserId()),
                UUID.fromString(payment.getPayeeId()))).thenReturn(mock(Payee.class));
        when(paymentValidator.isValidUuid(payment.getPaymentMethodId())).thenReturn(true);
        when(paymentService.findPaymentMethodByIdAndUserId(UUID.fromString(payment.getPaymentMethodId()),
                UUID.fromString(payment.getUserId()))).thenReturn(mock(PaymentMethod.class));
        when(paymentService.createMessage(payment)).thenReturn("success");
        ResponseEntity<PaymentResponse> response = paymentController.createPayment(payment);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response.getBody().getSuccessMessage(), "success");
    }

    @Test
    public void testGetPaymentMethodsForUser_InvalidUser() {
        String userId = "testUserId";
        ResponseEntity<PaymentMethodResponse> response = paymentController.getPaymentMethodsForUser(userId);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getError(), "User Id is not a valid format!");
    }

    @Test
    public void testGetPaymentMethodsForUser_Success() {
        String userId = "019b6d88-fd17-11ed-be56-0242ac120002";
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        when(paymentValidator.isValidUuid(anyString())).thenReturn(true);
        when(paymentService.findUserById(UUID.fromString(userId))).thenReturn(mock(PaymentUser.class));
        doNothing().when(paymentService).findPaymentMethodsForUser(UUID.fromString(userId), paymentMethods);
        ResponseEntity<PaymentMethodResponse> response = paymentController.getPaymentMethodsForUser(userId);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetPayeesForUser_InvalidUser() {
        String userId = "testUserId";
        ResponseEntity<PayeeResponse> response = paymentController.getPayeesForUser(userId);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody().getError(), "User Id is not a valid format!");
    }

    @Test
    public void testGetPayeesForUser_Success() {
        String userId = "019b6d88-fd17-11ed-be56-0242ac120002";
        List<PaymentUser> payees = new ArrayList<>();
        when(paymentValidator.isValidUuid(anyString())).thenReturn(true);
        when(paymentService.findUserById(UUID.fromString(userId))).thenReturn(mock(PaymentUser.class));
        doNothing().when(paymentService).findPayeesForUser(UUID.fromString(userId), payees);
        ResponseEntity<PayeeResponse> response = paymentController.getPayeesForUser(userId);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    private Payment createTestPayment() {
        return new Payment("019b6d88-fd17-11ed-be56-0242ac120002",
                "019b7152-fd17-11ed-be56-0242ac120002",
                "019b736e-fd17-11ed-be56-0242ac120002",
                "USD", new BigDecimal(0));
    }
}
