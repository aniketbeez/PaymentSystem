package com.github.aniketbeez.paymentwebservice.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private String userId;
    private String payeeId;
    private String paymentMethodId;
    private String currency;
    private BigDecimal amount;

    @Override
    public String toString() {
        return "Payment [userId=" + userId + ", payeeId=" + payeeId + ", paymentMethodId=" + paymentMethodId + ", " +
                "currency=" + currency + ", amount=" + amount + "]";
    }
}


