package com.github.aniketbeez.paymentwebservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.aniketbeez.paymentwebservice.domain.Payment;
import com.github.aniketbeez.paymentwebservice.model.Payee;
import com.github.aniketbeez.paymentwebservice.model.PaymentMethod;
import com.github.aniketbeez.paymentwebservice.model.PaymentUser;
import com.github.aniketbeez.paymentwebservice.repository.PayeeRepository;
import com.github.aniketbeez.paymentwebservice.repository.PaymentMethodRepository;
import com.github.aniketbeez.paymentwebservice.repository.PaymentUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock
    private  Producer producer;
    @Mock
    private  PaymentUserRepository paymentUserRepository;
    @Mock
    private PaymentMethodRepository paymentMethodRepository;
    @Mock
    private PayeeRepository payeeRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    public void testSendMessage() throws JsonProcessingException {
        Payment payment = mock(Payment.class);
        when(producer.sendMessage(payment)).thenReturn("message sent");
        assertEquals(paymentService.createMessage(payment), "message sent");
    }

    @Test
    public void testSendMessage_jsonProcessingException() throws JsonProcessingException {
        Payment payment = mock(Payment.class);
        when(producer.sendMessage(payment)).thenThrow(JsonProcessingException.class);
        assertThrows(JsonProcessingException.class, () -> paymentService.createMessage(payment));
    }
    @Test
    public void testFindPaymentMethodsForUser() {
        PaymentMethod paymentMethod = mock(PaymentMethod.class);
        UUID userIdMock = mock(UUID.class);
        List<PaymentMethod> expectedPaymentMethodList = new ArrayList<>();
        expectedPaymentMethodList.add(paymentMethod);
        when(paymentMethodRepository.findByUserId(userIdMock)).thenReturn(expectedPaymentMethodList);
        List<PaymentMethod> actualPaymentMethodList = new ArrayList<>();
        paymentService.findPaymentMethodsForUser(userIdMock, actualPaymentMethodList);
        assertEquals(expectedPaymentMethodList.size(), actualPaymentMethodList.size());
        assertEquals(expectedPaymentMethodList.size(),1);
        assertEquals(expectedPaymentMethodList.get(0), actualPaymentMethodList.get(0));
    }

    @Test
    public void testFindPayeesForUser(){
        PaymentUser payee = mock(PaymentUser.class);
        UUID userIdMock = mock(UUID.class);
        List<PaymentUser> expectedPayeeList = new ArrayList<>();
        expectedPayeeList.add(payee);
        when(paymentUserRepository.findPayeesBy(userIdMock)).thenReturn(expectedPayeeList);
        List<PaymentUser> actualPayeeList = new ArrayList<>();
        paymentService.findPayeesForUser(userIdMock, actualPayeeList);
        assertEquals(expectedPayeeList.size(), expectedPayeeList.size());
        assertEquals(expectedPayeeList.size(),1);
        assertEquals(expectedPayeeList.get(0), expectedPayeeList.get(0));
    }

    @Test
    public void testFindUserById() {
        PaymentUser user = mock(PaymentUser.class);
        UUID userIdMock = mock(UUID.class);
        when(paymentUserRepository.findById(userIdMock)).thenReturn(user);
        assertEquals(paymentService.findUserById(userIdMock), user);
    }

    @Test
    public void testFindPaymentMethodByIdAndUserId() {
        PaymentMethod paymentMethod = mock(PaymentMethod.class);
        UUID paymentMethodIdMock = mock(UUID.class);
        UUID userIdMock = mock(UUID.class);
        when(paymentMethodRepository.findByIdAndUserId(paymentMethodIdMock, userIdMock)).thenReturn(paymentMethod);
        assertEquals(paymentService.findPaymentMethodByIdAndUserId(paymentMethodIdMock, userIdMock), paymentMethod);
    }

    @Test
    public void testFindPayeeForUserById() {
        Payee payee = mock(Payee.class);
        UUID payeeIdMock = mock(UUID.class);
        UUID userIdMock = mock(UUID.class);
        when(payeeRepository.findByUserIdAndPayeeId(userIdMock, payeeIdMock)).thenReturn(payee);
        assertEquals(paymentService.findPayeeForUserById(userIdMock, payeeIdMock), payee);
    }

}
