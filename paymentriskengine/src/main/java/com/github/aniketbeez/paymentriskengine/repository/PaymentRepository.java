package com.github.aniketbeez.paymentriskengine.repository;

import com.github.aniketbeez.paymentriskengine.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
}
