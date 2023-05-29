package com.github.aniketbeez.paymentwebservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(PayeeKey.class)
@Table(name = "Payee", schema = "dbo")
public class Payee {
    @Id
    @Column(name = "userId")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID userId;
    @Id
    @Column(name = "payeeId")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID payeeId;
}
