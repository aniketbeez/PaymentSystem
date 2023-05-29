package com.github.aniketbeez.paymentwebservice.domain;

import com.github.aniketbeez.paymentwebservice.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodResponse {
    private List<PaymentMethod> paymentMethods;
    private String error;
}
