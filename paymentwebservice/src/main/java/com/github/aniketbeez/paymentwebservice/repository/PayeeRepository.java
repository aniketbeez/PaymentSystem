package com.github.aniketbeez.paymentwebservice.repository;

import com.github.aniketbeez.paymentwebservice.model.Payee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Payee respository interface for performing CRUD operations on Payee table
 */
public interface PayeeRepository extends JpaRepository<Payee, Long> {
    Payee findByUserId(UUID userId);
    Payee findByUserIdAndPayeeId(UUID userId, UUID payeeId);
}
