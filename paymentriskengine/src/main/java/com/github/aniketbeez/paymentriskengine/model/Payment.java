package com.github.aniketbeez.paymentriskengine.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "Payment", schema = "dbo")
public class Payment {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID id;

    @Column(name = "userId")
    private UUID userId;

    @Column(name = "payeeId")
    private UUID payeeId;

    @Column(name = "paymentMethodId")
    private UUID paymentMethodId;

    @Column(name = "currency")
    private String currency;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "riskScore", columnDefinition = "TINYINT")
    private int riskScore;

    @Column(name = "approved")
    private boolean approved;

    public Payment() {
    }
    public Payment(UUID userId, UUID payeeId, UUID paymentMethodId, String currency, BigDecimal amount) {
        this.userId = userId;
        this.payeeId = payeeId;
        this.paymentMethodId = paymentMethodId;
        this.currency = currency;
        this.amount = amount;
    }
    public UUID getId() {
        return id;
    }
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(UUID payeeId) {
        this.payeeId = payeeId;
    }

    public UUID getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(UUID paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Payment [id=" + id + ", userId=" + userId + ", " +
                "payeeId=" + payeeId + ", paymentMethodId=" + paymentMethodId +
                "currency=" + currency + ", amount=" + amount +"]";
    }
}