package com.github.aniketbeez.paymentwebservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PaymentMethod", schema = "dbo")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "userId")
    private UUID userId;

    @Column(name = "paymentType")
    private String paymentType;

    @Column(name = "paymentMethodName")
    private String paymentMethodName;

    @Column(name = "lastFourDigits")
    private String lastFourDigits;

    @Override
    public String toString() {
        return "PaymentMethod [id=" + id + ", userId=" + userId + ", paymentType=" + paymentType +
                ", paymentMethodName=" + paymentMethodName + ", lastFourDigits=" + lastFourDigits +"]";
    }
}
