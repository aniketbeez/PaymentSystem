package com.github.aniketbeez.paymentwebservice.repository;

import com.github.aniketbeez.paymentwebservice.model.PaymentMethod;
import com.github.aniketbeez.paymentwebservice.model.PaymentUser;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PaymentUserRepository extends JpaRepository<PaymentUser, Long>{
    PaymentUser findById(UUID userId);

    @Query("SELECT user FROM PaymentUser user WHERE user.id IN (SELECT payeeId FROM Payee WHERE userId = :userId)" )
    List<PaymentUser>  findPayeesBy(@Param("userId") UUID userId);
}
