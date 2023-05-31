package com.github.aniketbeez.paymentriskengine.repository;

import com.github.aniketbeez.paymentriskengine.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Payment repository interface for performing underlying CRUD operation
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
}
