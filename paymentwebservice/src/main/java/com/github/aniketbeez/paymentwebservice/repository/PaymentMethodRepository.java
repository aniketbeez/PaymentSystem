package com.github.aniketbeez.paymentwebservice.repository;

import com.github.aniketbeez.paymentwebservice.domain.Payment;
import com.github.aniketbeez.paymentwebservice.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    PaymentMethod findByIdAndUserId(UUID id, UUID userId);
    List<PaymentMethod> findByUserId(UUID userId);
}
