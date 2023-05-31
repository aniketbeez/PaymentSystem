package com.github.aniketbeez.paymentriskengine.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Value
@AllArgsConstructor
public class PaymentDto {

    @NotNull
    BigDecimal  amount;

    @NotEmpty
    String currency;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID payeeId;

    @NotNull
    private UUID paymentMethodId;

}
