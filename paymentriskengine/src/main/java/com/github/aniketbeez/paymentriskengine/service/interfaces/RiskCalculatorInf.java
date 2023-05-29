package com.github.aniketbeez.paymentriskengine.service.interfaces;

import com.github.aniketbeez.paymentriskengine.model.Payment;

public interface RiskCalculatorInf {
    public int calculateRiskScore(Payment payment);

}
