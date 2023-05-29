package com.github.aniketbeez.paymentwebservice.domain;

import com.github.aniketbeez.paymentwebservice.model.PaymentUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayeeResponse {
    private List<PaymentUser>  payees;
    private String error;
}
