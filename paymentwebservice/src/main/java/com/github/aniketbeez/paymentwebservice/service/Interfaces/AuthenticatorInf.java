package com.github.aniketbeez.paymentwebservice.service.Interfaces;

import org.springframework.security.core.Authentication;

public interface AuthenticatorInf {
    String generateToken(Authentication authentication);
}
