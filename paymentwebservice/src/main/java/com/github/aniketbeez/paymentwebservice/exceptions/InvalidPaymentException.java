package com.github.aniketbeez.paymentwebservice.exceptions;

public class InvalidPaymentException extends Exception{
    public InvalidPaymentException(String error) {
        super(error);
    }
}