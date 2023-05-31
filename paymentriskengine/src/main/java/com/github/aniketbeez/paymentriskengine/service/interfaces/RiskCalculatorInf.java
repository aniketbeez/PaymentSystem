package com.github.aniketbeez.paymentriskengine.service.interfaces;

import com.github.aniketbeez.paymentriskengine.model.Payment;

public interface RiskCalculatorInf {
    /**
     * Interface for Risk Calculation
     * @param payment
     * @return riskScore
     */
    int calculateRiskScore(Payment payment);

}
