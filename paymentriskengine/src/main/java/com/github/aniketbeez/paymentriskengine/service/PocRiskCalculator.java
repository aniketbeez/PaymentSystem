package com.github.aniketbeez.paymentriskengine.service;

import com.github.aniketbeez.paymentriskengine.model.Payment;
import com.github.aniketbeez.paymentriskengine.service.interfaces.RiskCalculatorInf;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PocRiskCalculator implements RiskCalculatorInf {

    public int calculateRiskScore(Payment payment) {
        return new Random().nextInt(100); //ignore any logic, and returns a random number that represents the risk score
    }

}
