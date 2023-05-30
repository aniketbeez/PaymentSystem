package com.github.aniketbeez.paymentwebservice.controller;

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
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@RestController
@Validated
@RequestMapping("/api")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentValidator paymentValidator;

    private final Bucket bucket;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentValidator paymentValidator) {
        this.paymentService = paymentService;
        this.paymentValidator = paymentValidator;

        Bandwidth limit = Bandwidth.classic(3, Refill.greedy(3, Duration.ofSeconds(10)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping("/create-payment")
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody Payment payment) {
        log.info("Payment creation request received : " + payment.toString());
        if(bucket.tryConsume(1)) {
            try{
                //validate if payment request has all the required fields
                if(!paymentValidator.isValidPaymentRequest(payment)){
                    throw new InvalidPaymentException("Please provide all required fields!");
                }
                //validate if currency is valid
                if(!paymentValidator.isValidCurrency(payment.getCurrency())) {
                    throw new InvalidPaymentException("Please provide valid currency code!");
                }
                //validate if userId and Payee is valid
                validateUser(payment.getUserId());
                validateUser(payment.getPayeeId());
                //validate if user-payee relation is valid
                validateUserPayee(payment.getUserId(), payment.getPayeeId());
                //validate payment method
                validatePaymentMethod(payment.getPaymentMethodId(), payment.getUserId());

                //if all the request parameters are valid, create a payment message in the queue
                String response = paymentService.createMessage(payment);
                return new ResponseEntity<>(new PaymentResponse(null, response), HttpStatus.CREATED);
            } catch (InvalidPaymentException ipe) {
                return new ResponseEntity<>(new PaymentResponse(ipe.getMessage(), null), HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return new ResponseEntity<>(new PaymentResponse(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(new PaymentResponse("Too many requests!", null), HttpStatus.TOO_MANY_REQUESTS);

    }

    @GetMapping("/get-payment-methods")
    public ResponseEntity<PaymentMethodResponse> getPaymentMethodsForUser(@RequestParam String userId) {
        log.info("Get payment method request received for userId : " + userId);
        if(bucket.tryConsume(1)) {
            try {
                validateUser(userId);
                List<PaymentMethod> paymentMethods = new ArrayList<>();
                paymentService.findPaymentMethodsForUser(UUID.fromString(userId), paymentMethods);
                return new ResponseEntity<>(new PaymentMethodResponse(paymentMethods, null), HttpStatus.OK);
            } catch (InvalidPaymentException ipe) {
                return new ResponseEntity<>(new PaymentMethodResponse(null, ipe.getMessage()), HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return new ResponseEntity<>(new PaymentMethodResponse(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(new PaymentMethodResponse(null, "Too many requests!"), HttpStatus.TOO_MANY_REQUESTS);
    }

    @GetMapping("/get-payees")
    public ResponseEntity<PayeeResponse> getPayeesForUser(@RequestParam String userId) {
        log.info("Get Payees request received for userId : " + userId);
        try {
            validateUser(userId);
            List<PaymentUser> payees = new ArrayList<>();
            paymentService.findPayeesForUser(UUID.fromString(userId), payees);
            return new ResponseEntity<>(new PayeeResponse(payees, null), HttpStatus.OK);
        } catch (InvalidPaymentException ipe) {
            return new ResponseEntity<>(new PayeeResponse(null, ipe.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new PayeeResponse(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private void validateUser(String userId) throws InvalidPaymentException{
        if(!paymentValidator.isValidUuid(userId)) {
            throw new InvalidPaymentException("User Id is not a valid format!");
        }
        Optional<PaymentUser> userData = Optional.ofNullable(
                paymentService.findUserById(UUID.fromString(userId)));
        if(!userData.isPresent()) {
            throw new InvalidPaymentException("Provided user is not a registered user!");
        }
    }

    private void validatePaymentMethod(String paymentMethodId, String userId) throws InvalidPaymentException{
        //validate payment method
        if(!paymentValidator.isValidUuid(paymentMethodId)) {
            throw new InvalidPaymentException("Payment method id is invalid format!");
        }
        //Validate if payment method id for the correct user
        Optional<PaymentMethod> paymentMethodData = Optional.ofNullable(
                paymentService.findPaymentMethodByIdAndUserId(UUID.fromString(paymentMethodId), UUID.fromString(userId)));
        if(!paymentMethodData.isPresent()) {
            throw new InvalidPaymentException("Provided payment method is not registered for this user!");
        }
    }

    private void validateUserPayee(String userId, String payeeId) throws InvalidPaymentException {
        Optional<Payee> userPayeeData = Optional.ofNullable(
                paymentService.findPayeeForUserById(UUID.fromString(userId), UUID.fromString(payeeId)));
        if(!userPayeeData.isPresent()) {
            throw new InvalidPaymentException("Provided payee is not registered for this user!");
        }
    }

}
