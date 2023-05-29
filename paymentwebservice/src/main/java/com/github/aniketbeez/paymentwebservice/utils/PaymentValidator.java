package com.github.aniketbeez.paymentwebservice.utils;

import com.github.aniketbeez.paymentwebservice.domain.Payment;
import com.github.aniketbeez.paymentwebservice.enums.Currency;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.regex.Pattern;
@Component
public class PaymentValidator {
    private static final Pattern EMAIL_REGEX_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$");
    public boolean isValidPaymentRequest(Payment payment) {
        if (StringUtils.isEmpty(payment.getAmount())
                || StringUtils.isEmpty(payment.getCurrency())
                || StringUtils.isEmpty(payment.getUserId())
                || StringUtils.isEmpty(payment.getPayeeId())
                || StringUtils.isEmpty(payment.getPaymentMethodId())) {
            return false;
        }
        return true;
    }
    public boolean isValidCurrency(String currency) {
        for (Currency validCurrency : Currency.values()) {
            if (validCurrency.name().equalsIgnoreCase(currency)) return true;
        }
        return false;
    }
    public boolean isValidEmail(String email) {
        return EMAIL_REGEX_PATTERN.matcher(email).matches();
    }
    public boolean isValidUuid(String possibleUuid) {
        try{
            UUID uuid = UUID.fromString(possibleUuid);
            return true;
        } catch (IllegalArgumentException exception){
            return false;
        }
    }
}
