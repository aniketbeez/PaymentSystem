package com.github.aniketbeez.paymentriskengine.service;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PaymentCounter {

    private AtomicInteger approvedPaymentCounter = new AtomicInteger(0);
    private AtomicInteger rejectedPaymentCounter = new AtomicInteger(0);


    public AtomicInteger getApprovedPaymentCounter() {
        return approvedPaymentCounter;
    }

    public void setApprovedPaymentCounter(AtomicInteger approvedPaymentCounter) {

        this.approvedPaymentCounter = approvedPaymentCounter;
    }

    public AtomicInteger getRejectedPaymentCounter() {

        return rejectedPaymentCounter;
    }

    public void setRejectedPaymentCounter(AtomicInteger rejectedPaymentCounter) {

        this.rejectedPaymentCounter = rejectedPaymentCounter;
    }
}
