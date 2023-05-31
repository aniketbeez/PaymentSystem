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

    /**
     * Checks all fields in the request body are not null or empty
     * @param payment
     * @return boolean reflecting if the payment is valid
     */
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

    /**
     * Checks validity of currency field with respect to Currency enum
     * @param currency
     * @return true if valid currency, else false
     */
    public boolean isValidCurrency(String currency) {
        for (Currency validCurrency : Currency.values()) {
            if (validCurrency.name().equalsIgnoreCase(currency)) return true;
        }
        return false;
    }
    /**
     * Checks the email field is in the right format
     * @param email
     * @return true if email is in valid format, else false
     */
    public boolean isValidEmail(String email) {
        return EMAIL_REGEX_PATTERN.matcher(email).matches();
    }

    /**
     * Checks the if the UUID passed is in right format
     * @param possibleUuid
     * @return true is correct UUID format, else false
     */
    public boolean isValidUuid(String possibleUuid) {
        try{
            UUID uuid = UUID.fromString(possibleUuid);
            return true;
        } catch (IllegalArgumentException exception){
            return false;
        }
    }
}
